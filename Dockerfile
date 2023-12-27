# m1 이라면 openjdk:17-jdk-alpine 대신 openjdk:17 사용
FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 1234
ENTRYPOINT ["java ${JAVA_OPTS} ${JAVA_TIMEZ} ${JAVA_ACTIVE} -jar /app.jar"]