package cloud.tom.diagnosis.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "tom.diagnosis")
data class ApplicationProperties(
    val pulsarProperties: PulsarProperties,
    val s3Properties: S3Properties
) {
    data class PulsarProperties(
        val url: String,
        val token: String,
        val cluster: String,
    )
    data class S3Properties(
        val secretId: String,
        val secretKey: String,
        val bucket: String,
        val endpoint: String,
        val region: String
    )
}

