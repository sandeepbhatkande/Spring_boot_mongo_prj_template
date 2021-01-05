FROM library/openjdk:8u171-jdk-alpine
VOLUME /tmp
COPY build/libs/* app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
