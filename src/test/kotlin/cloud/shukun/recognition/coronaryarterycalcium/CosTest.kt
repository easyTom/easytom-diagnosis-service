package cloud.shukun.recognition.coronaryarterycalcium

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CosTests {


    @Test
    fun dd() {
        val COS_ENDPOINT = "http://127.0.0.1:9000"
        val COS_SIGNING_REGION = "ap-shanghai"
        val ACCESS_KEY = "your_access_key"
        val SECRET_KEY = "your_secret_key"

        val s3Client = AmazonS3ClientBuilder.standard().withCredentials(
            AWSStaticCredentialsProvider(
                BasicAWSCredentials(
                    ACCESS_KEY, SECRET_KEY
                )
            )
        ).withEndpointConfiguration(
            AwsClientBuilder.EndpointConfiguration(
                COS_ENDPOINT, COS_SIGNING_REGION
            )
        ).build()

        println(s3Client.listBuckets())

        s3Client.createBucket("bucket-tom1")

        println(s3Client.listBuckets())



    }

}