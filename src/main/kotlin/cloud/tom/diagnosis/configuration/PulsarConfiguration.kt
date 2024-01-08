package cloud.tom.diagnosis.configuration

import cloud.tom.diagnosis.Notification.DiagnosisUpdatedNotification
import org.apache.pulsar.client.api.AuthenticationFactory
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PulsarConfiguration @Autowired constructor(
    private val applicationProperties: ApplicationProperties,
) {
    @Bean
    fun pulsarClient(): PulsarClient {
        return PulsarClient.builder()
            .serviceUrl(applicationProperties.pulsarProperties.url)
            .build()
    }


    @Bean
    fun workTimeoutNotificationPublisher(pulsarClient: PulsarClient): Producer<DiagnosisUpdatedNotification> {
        return pulsarClient
            .newProducer(Schema.PROTOBUF(DiagnosisUpdatedNotification::class.java))
            .topic("persistent://public/default/diagnosisUpdatedNotification")
            .create()
    }


}


