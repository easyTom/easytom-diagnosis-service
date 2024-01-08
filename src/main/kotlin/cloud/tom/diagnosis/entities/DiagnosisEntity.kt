package cloud.tom.diagnosis.entities

import org.hibernate.annotations.*
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table(schema = "tom_study", name = "diagnosis")
@Where(clause = "deletion_datetime is null")
@SQLDelete(sql = "update diagnosis set deletion_datetime = now(6) where uuid=?")
class DiagnosisEntity(

    @Id @GeneratedValue(generator = "uuid") @GenericGenerator(
        name = "uuid", strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
        name = "uuid", updatable = false, nullable = false, columnDefinition = "BINARY(16)"
    ) var uuid: UUID? = null,

    @Column(
        name = "message" ,columnDefinition = "VARCHAR(128)"
    ) var message: String? = null,

    @Column(
        name = "diagnosis_uuid" ,columnDefinition = "BINARY(16)"
    ) var diagnosisId: UUID? = null,


    /**
     * 创建时间
     */
    @CreationTimestamp @Column(name = "creation_datetime") var creationDateTime: LocalDateTime,

    /**
     * 更新时间
     */
    @UpdateTimestamp @Column(name = "update_datetime") var updateDateTime: LocalDateTime? = null,

    /**
     * 删除时间
     */
    @Column(name = "deletion_datetime") var deletionDateTime: LocalDateTime? = null,
    @Column(name = "state") @Convert(converter = DiagnosisState.StateConverter::class) var state: DiagnosisState

) {


    enum class DiagnosisState(val value: Int, val description: String) {
        DEFAULT(value = 0, description = "默认"),
        PROCESSING(value = 1, description = "处理中"),
        DONE(value = 5, description = "处理结束");

        class StateConverter : AttributeConverter<DiagnosisState, Int> {
            override fun convertToDatabaseColumn(diagnosisState: DiagnosisState?): Int {
                return diagnosisState?.value ?: 0
            }

            override fun convertToEntityAttribute(dbData: Int?): DiagnosisState {
                return values().first { (dbData ?: 0) == it.value }
            }
        }
    }

}

