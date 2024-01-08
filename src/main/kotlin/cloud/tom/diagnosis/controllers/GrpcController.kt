package cloud.tom.diagnosis.controllers

import cloud.tom.diagnosis.configuration.Exceptions
import cloud.tom.health.demo.demo.DemoGrpc.DemoImplBase
import cloud.tom.health.demo.demo.DemoOuterClass
import io.grpc.stub.StreamObserver
import org.lognet.springboot.grpc.GRpcService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.*


@GRpcService
class GrpcController : DemoImplBase() {

    private val log: Logger = LoggerFactory.getLogger(GrpcController::class.java)

    override fun hello(
        request: DemoOuterClass.HelloRequest?, responseObserver: StreamObserver<DemoOuterClass.HelloResponse>?
    ) {
        request ?: throw Exceptions.RequestNullException("38bc7daf-1d18-408b-bf85-8eaefc8b6e20")
        log.info(request.message)
        Thread.sleep(2000)
        val response = DemoOuterClass.HelloResponse.newBuilder().apply {
            this.message = request.message
        }
            .build()
        responseObserver?.onNext(response)
        responseObserver?.onCompleted()

    }
}

