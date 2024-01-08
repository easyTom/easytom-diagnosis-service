package cloud.tom.diagnosis.services

import cloud.tom.diagnosis.configuration.Exceptions
import cloud.tom.diagnosis.entities.DiagnosisEntity
import cloud.tom.diagnosis.repositories.DiagnosisRepository
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.redisson.api.RedissonClient
import org.redisson.codec.TypedJsonJacksonCodec
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.annotation.PostConstruct
import kotlin.concurrent.withLock

@Service
class DiagnosisService @Autowired constructor(
    private val redissonClient: RedissonClient, private val diagnosisRepository: DiagnosisRepository
) {

    class TaskQueueElementComparator : Comparator<TaskQueueElement> {
        override fun compare(o1: TaskQueueElement, o2: TaskQueueElement): Int {
            val nameComparison = when {
                o1.name == "至尊" && o2.name != "至尊" -> -1
                o1.name != "至尊" && o2.name == "至尊" -> 1
                o1.name == "会员" && o2.name != "会员" -> -1
                o1.name != "会员" && o2.name == "会员" -> 1
                else -> 0
            }

            return if (nameComparison != 0) {
                nameComparison
            } else {
                // 如果 name 相同，则按照 submitDateTime 属性进行排序
                o1.submitDateTime.compareTo(o2.submitDateTime)
            }
        }

//        override fun compare(
//            o1: TaskQueueElement?, o2: TaskQueueElement?
//        ): Int {
//            val time1 = o1!!.submitDateTime.toEpochSecond(ZoneOffset.UTC)
//            val time2 = o2!!.submitDateTime.toEpochSecond(ZoneOffset.UTC)
//            return (time1 - time2).toInt()
//        }
    }


    private val log = LoggerFactory.getLogger(DiagnosisService::class.java)

    private val taskLockerName = "tom.diagnosis_locker"
    private val taskQueueName = "tom.diagnosis_queue"
    private val taskQueueCodec = TypedJsonJacksonCodec(TaskQueueElement::class.java)


    data class DataContext(val uuid: UUID, val message: String, val name: String = "")

    data class TaskQueueElement(
        var uuid: UUID = UUID.randomUUID(),
        var name: String = "",
        @get:JsonSerialize(using = LocalDateTimeSerializer::class) @set:JsonDeserialize(using = LocalDateTimeDeserializer::class) var submitDateTime: LocalDateTime = LocalDateTime.now(),
    )

    @PostConstruct
    fun postConstruct() {
        val taskQueue = redissonClient.getPriorityQueue<TaskQueueElement>(taskQueueName, taskQueueCodec)
        taskQueue.trySetComparator(TaskQueueElementComparator())
    }

    // 收到诊断提交
    fun submit(dataContext: DataContext): UUID {
        val diagnosisEntity = DiagnosisEntity(
            state = DiagnosisEntity.DiagnosisState.PROCESSING,
            diagnosisId = dataContext.uuid,
            creationDateTime = LocalDateTime.now(),
            message = dataContext.message


        )
        diagnosisRepository.save(diagnosisEntity)
        val queueElement = TaskQueueElement(
            uuid = dataContext.uuid,
            submitDateTime = diagnosisEntity.creationDateTime,
            name =  dataContext.name
        )
        redissonClient.getLock(taskLockerName).withLock {
            val taskQueue = redissonClient.getPriorityQueue<TaskQueueElement>(taskQueueName, taskQueueCodec)
            taskQueue.offer(queueElement)
        }
        return diagnosisEntity.uuid!!
    }


    fun claim(): DataContext? {
        // ✅ 从优先级队列中取出最高优先级任务
        val taskQueueElement = redissonClient.getLock(taskLockerName).withLock {
            redissonClient.getPriorityQueue<TaskQueueElement>(taskQueueName, taskQueueCodec).poll()
        } ?: return null
        val diagnosisEntity = diagnosisRepository.getFirstByDiagnosisIdIs(taskQueueElement.uuid)
            ?: throw Exceptions.DiagnosisNotFoundException("5e2788df-e5dd-4dbc-8301-090f13058fde")
        diagnosisEntity.state = DiagnosisEntity.DiagnosisState.DONE
        diagnosisRepository.save(diagnosisEntity)
        return DataContext(
            taskQueueElement.uuid, taskQueueElement.submitDateTime.toString()
        )
    }

    fun onDiagnosisUpdatedNotificationReceived(uuid: String) {
        log.info(uuid)
    }

}


