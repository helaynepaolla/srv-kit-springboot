FROM nexusrepository.bradesco.com.br:8500/ubi8/openjdk-17-runtime:1.14-9 as builder
ARG JAR_FILE=target/srv-kit-springboot.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract && \
    rm -f app.jar /home/jboss/application/BOOT-INF/classes/application-default.yml

FROM nexusrepository.bradesco.com.br:8500/ubi8/openjdk-17-runtime:1.14-9 as runtime

# É possivel colocar mais do que 1 java agents dentro da jvm.
ARG JAVA_AGENT="target/opentelemetry-javaagent.jar"
ENV JAVAAGENT_OPTS="-javaagent:javaagent.jar -Dotel.traces.exporter=none -Dotel.metrics.exporter=none"
ENV JAVA_OPTS="-Xms768m -Xmx768m"
ENV TIMEZONE=America/Sao_Paulo

COPY ${JAVA_AGENT} ./javaagent.jar

COPY --from=builder /home/jboss/dependencies/ ./
COPY --from=builder /home/jboss/spring-boot-loader/ ./
COPY --from=builder /home/jboss/snapshot-dependencies/ ./
COPY --from=builder /home/jboss/application/ ./

ENTRYPOINT exec java $JAVAAGENT_OPTS $JAVA_OPTS \
    -Djava.security.egd=file:/dev/./urandom \
    -Duser.timezone=$TIMEZONE \
    org.springframework.boot.loader.JarLauncher