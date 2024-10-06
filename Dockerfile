FROM  amazoncorretto:17-alpine-jdk
LABEL authors="Alex (N911)"
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]