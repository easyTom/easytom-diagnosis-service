package cloud.shukun.recognition.coronaryarterycalcium

import cloud.shukun.health.examination.computedtomography.ComputedTomographyExaminationGrpc
import cloud.shukun.health.examination.computedtomography.ExaminationComputedTomography
import cloud.tom.diagnosis.Application
import cloud.tom.diagnosis.services.DiagnosisService
import cloud.tom.health.demo.demo.DemoGrpc
import cloud.tom.health.demo.demo.DemoOuterClass
import io.grpc.ManagedChannelBuilder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest(classes = [Application::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureCache
class ProducerTests(
    @Autowired val d: DiagnosisService, @Autowired val bb: DemoGrpc.DemoBlockingStub
) {

    @Test
    fun redisPush() {
        for (i in 1..10) {
            val da = DiagnosisService.DataContext(
                UUID.randomUUID(), "第 $i 条数据"
            )
            d.submit(da)
            d.submit(
                DiagnosisService.DataContext(
                    UUID.randomUUID(), "heh", "至尊"
                )
            )
            d.submit(
                DiagnosisService.DataContext(
                    UUID.randomUUID(), "hahaha", "会员"
                )
            )
        }

    }

    @Test
    fun redisPull() {
        for (i in 1..40) {

            println(d.claim())
        }

    }
    @Test
    fun grpcc() {
        val channel = ManagedChannelBuilder.forAddress(
            "150.158.239.", 50058
        )
            .usePlaintext()
            // 避免长连接导致pod扩容打不到情况
            .idleTimeout(1,TimeUnit.SECONDS)
            .build()

        val ds = DemoGrpc.newBlockingStub(channel)

        while (true){
            Thread.sleep(2000)
            val result = ds.hello(DemoOuterClass.HelloRequest.newBuilder().apply {
                this.message = "19a0b664-a982-45c8-b592-969ccf9d7ebe"
            }.build())
            println(result)
        }


    }


}