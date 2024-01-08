package cloud.tom.diagnosis.repositories

import cloud.tom.diagnosis.entities.DiagnosisEntity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface DiagnosisRepository : JpaRepository<DiagnosisEntity, UUID> {
    fun getFirstByDiagnosisIdIs(diagnosisId:UUID):DiagnosisEntity?
}


