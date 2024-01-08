package cloud.tom.diagnosis.controllers

import cloud.tom.diagnosis.Notification
import cloud.tom.diagnosis.configuration.ApplicationProperties
import cloud.tom.diagnosis.services.DiagnosisService
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SubscriptionType
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.event.ContextClosedEvent
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit


@Component
class DiagnosisUpdatedNotificationSubscriber @Autowired constructor(
    private val pulsarClient: PulsarClient,
    private val diagnosisService: DiagnosisService,
) {

    private val log = LoggerFactory.getLogger(DiagnosisUpdatedNotificationSubscriber::class.java)
    var shutdown = false
    var consumers = mutableListOf<Consumer<Notification.DiagnosisUpdatedNotification>>()

    @Bean
    fun subscribeDiagnosisUpdatedNotification() {
        val topic =
            "persistent://public/default/diagnosisUpdatedNotification"
        val subscriptionName = "easyTom-diagnosis-service"
        log.info("start subscribe to $topic with subscriptionName: $subscriptionName")
        for (index in 0 until 10) {
            val consumer = pulsarClient
                .newConsumer(Schema.PROTOBUF(Notification.DiagnosisUpdatedNotification::class.java))
                .topic(topic)
                .subscriptionName(subscriptionName)
                .consumerName("$subscriptionName:$index")
                .receiverQueueSize(1)
                .subscriptionType(SubscriptionType.Shared)
                .ackTimeout(30, TimeUnit.SECONDS)
                .messageListener { consumer, message ->

                    synchronized(this@DiagnosisUpdatedNotificationSubscriber) {
                        // 优雅停机期间不消费消息，拒绝已预取的消息。
                        if (shutdown) {
                            log.warn("Ignore message ${message.value} with NegativeAcknowledge when shutdown")
                            consumer.negativeAcknowledge(message)
                            return@messageListener
                        }
                    }
                    try {
                        diagnosisService.onDiagnosisUpdatedNotificationReceived(message.value.uuid)

                        consumer.acknowledge(message)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        consumer.acknowledge(message)
                    } finally {
                        consumer.acknowledge(message)
                    }

                }
                .subscribe()
            consumers.add(consumer)
        }
    }

    @org.springframework.context.event.EventListener(ContextClosedEvent::class)
    fun onContextClosed() {
        log.warn("onContextClosed, waiting for graceful shutdown.")
        log.info("DiagnosisUpdatedNotificationSubscriber 停止接收消息前")
        synchronized(this) {
            // 停止接收消息
            log.info("DiagnosisUpdatedNotificationSubscriber 正在停止接受请求")
            consumers.forEach { it.pause() }
            this.shutdown = true
        }
        log.info("DiagnosisUpdatedNotificationSubscriber 停止接收消息后")
        // 等待消费中的业务完成
        Thread.sleep(12 * 1000)
        // 清理
        consumers.forEach { it.close() }
        log.warn("DiagnosisUpdatedNotificationSubscriber shutdown")
    }
}

