package cloud.tom.diagnosis.configuration

import cloud.tom.health.demo.demo.DemoGrpc
import cloud.tom.health.demo.demo.DemoGrpc.DemoBlockingStub
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.grpc.ServerBuilder
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.io.File
import java.util.concurrent.TimeUnit

@Configuration
class GrpcClientConfiguration {

    @Bean
    fun demoServiceClient(): DemoBlockingStub {
        val channel = ManagedChannelBuilder.forAddress(
            "127.0.0.1",
            50055
        )
            .usePlaintext()
            .idleTimeout(10, TimeUnit.SECONDS)
            .build()
        return DemoGrpc
            .newBlockingStub(channel)
            // default 4M
            .withMaxInboundMessageSize(Int.MAX_VALUE)
            .withMaxOutboundMessageSize(Int.MAX_VALUE)
    }
}