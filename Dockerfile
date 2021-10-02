FROM store/oracle/jdk:11

ENV JAVA_OPTS=""

COPY target/*.jar /app/onfood.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/urandom -jar /app/onfood.jar"]