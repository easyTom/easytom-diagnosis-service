//package cloud.tom.diagnosis.repositories
//
//import cloud.shukun.health.recognition.lung.Annotation.SeriesAnnotationDocument
//import cloud.tom.diagnosis.configuration.ApplicationProperties
//import com.qcloud.cos.COSClient
//import com.qcloud.cos.model.ObjectMetadata
//import com.qcloud.cos.model.PutObjectRequest
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Repository
//import java.io.ByteArrayInputStream
//import java.io.ByteArrayOutputStream
//import java.util.*
//
//
//@Repository
//class SeriesAnnotationDocumentRepository @Autowired constructor(
//    private val applicationProperties: ApplicationProperties,
//    private val cosClient: COSClient,
//) {
//    private val log = LoggerFactory.getLogger(SeriesAnnotationDocumentRepository::class.java)
//    private val bucket = applicationProperties.cosProperties.bucket
//    fun save(
//        taskUUID: UUID,
//        workUUID: UUID,
//        studyInstanceUid: String,
//        seriesInstanceUid: String,
//        seriesAnnotationDocument:SeriesAnnotationDocument
//    ) {
//        val outputStream = ByteArrayOutputStream()
//        seriesAnnotationDocument.writeTo(outputStream)
//
//        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
//
//        val key = "algorithm/annotation/${studyInstanceUid}/${seriesInstanceUid}/${taskUUID}/${workUUID}.pb"
//
//        val metadata = ObjectMetadata().apply {
//            contentLength = outputStream.size().toLong()
//        }
//        val putObjectRequest = PutObjectRequest(bucket, key, inputStream, metadata)
//        val putObjectResult = cosClient.putObject(putObjectRequest)
//
//        log.info("uploaded annotation file for studyInstanceUid: $studyInstanceUid, seriesInstanceUid: $seriesInstanceUid, task: $taskUUID, work: $workUUID to bucket: $bucket key: $key with result: ${putObjectResult.dateStr}")
//    }
//
//    fun saveOriginal(
//        taskUUID: UUID,
//        workUUID: UUID,
//        studyInstanceUid: String,
//        seriesInstanceUid: String,
//        seriesAnnotationDocument:SeriesAnnotationDocument
//    ){
//        val outputStream = ByteArrayOutputStream()
//        seriesAnnotationDocument.writeTo(outputStream)
//
//        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
//
//        val key = "algorithm/annotation/${studyInstanceUid}/${seriesInstanceUid}/${taskUUID}/original/${workUUID}.pb"
//
//        val metadata = ObjectMetadata().apply {
//            contentLength = outputStream.size().toLong()
//        }
//        val putObjectRequest = PutObjectRequest(bucket, key, inputStream, metadata)
//        val putObjectResult = cosClient.putObject(putObjectRequest)
//
//        log.info("uploaded annotation file for studyInstanceUid: $studyInstanceUid, seriesInstanceUid: $seriesInstanceUid, task: $taskUUID, work: $workUUID to bucket: $bucket key: $key with result: ${putObjectResult.dateStr}")
//    }
//
//
//    fun find(
//        studyInstanceUid: String,
//        seriesInstanceUid: String,
//        taskUUID: UUID,
//        workUUID: UUID,
//    ): SeriesAnnotationDocument {
//        val key = "algorithm/annotation/${studyInstanceUid}/${seriesInstanceUid}/${taskUUID}/${workUUID}.pb"
//        val cosObject = cosClient.getObject(bucket, key)
//        return SeriesAnnotationDocument.parseFrom(cosObject.objectContent)
//    }
//
//    fun findOriginal(
//        studyInstanceUid: String,
//        seriesInstanceUid: String,
//        taskUUID: UUID,
//        workUUID: UUID,
//    ): SeriesAnnotationDocument {
//        val key = "algorithm/annotation/${studyInstanceUid}/${seriesInstanceUid}/${taskUUID}/original/${workUUID}.pb"
//        val cosObject = cosClient.getObject(bucket, key)
//        return SeriesAnnotationDocument.parseFrom(cosObject.objectContent)
//    }
//
//}
