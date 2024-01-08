package cloud.tom.diagnosis.configuration

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.auth.COSCredentials
import com.qcloud.cos.http.HttpProtocol
import com.qcloud.cos.region.Region
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class S3Configuration @Autowired constructor(
    private val applicationProperties: ApplicationProperties,
) {
    @Bean
    fun s3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard().withCredentials(
            AWSStaticCredentialsProvider(
                BasicAWSCredentials(
                    applicationProperties.s3Properties.secretId, applicationProperties.s3Properties.secretKey
                )
            )
        ).withEndpointConfiguration(
            AwsClientBuilder.EndpointConfiguration(
                applicationProperties.s3Properties.endpoint, applicationProperties.s3Properties.region
            )
        ).build()
    }
}