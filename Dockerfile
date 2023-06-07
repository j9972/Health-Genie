FROM openjdk:18-jdk-slim
ADD /build/libs/*.jar app.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
ENTRYPOINT ["java","-jar","/app.jar"]