package cloud.tom.diagnosis.configuration

import io.lettuce.core.ClientOptions
import io.lettuce.core.TimeoutOptions
import io.lettuce.core.protocol.ProtocolVersion
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration


@Configuration
class RedisConfiguration: LettuceClientConfigurationBuilderCustomizer {
    private val log = LoggerFactory.getLogger(RedisConfiguration::class.java)

    override fun customize(clientConfigurationBuilder: LettuceClientConfiguration.LettuceClientConfigurationBuilder?) {
        clientConfigurationBuilder?.apply {
            clientOptions(
                ClientOptions.builder()
                    .timeoutOptions(TimeoutOptions.enabled())
                    .protocolVersion(ProtocolVersion.RESP2)
                    .build()
            )
        }
    }

}

