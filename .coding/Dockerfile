FROM gradle:7.3.3-jdk11 as builder
WORKDIR /home/gradle/workspace
COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon

RUN java -Djarmode=layertools -jar ./build/libs/app.jar extract --destination ./build/libs

FROM adoptopenjdk/openjdk11:alpine-jre

#RUN wget -qO/bin/grpc_health_probe https://public-1309797432.cos.ap-shanghai.myqcloud.com/grpc_health_probe-linux-amd64 && \
#    chmod +x /bin/grpc_health_probe

RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*

WORKDIR /app

COPY --from=builder /home/gradle/workspace/build/libs/dependencies/ ./
COPY --from=builder /home/gradle/workspace/build/libs/spring-boot-loader/ ./
COPY --from=builder /home/gradle/workspace/build/libs/snapshot-dependencies/ ./
COPY --from=builder /home/gradle/workspace/build/libs/application/ ./

EXPOSE 50051
ENV TZ=Asia/Shanghai

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

