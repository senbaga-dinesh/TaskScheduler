# Use an OpenJDK base image
FROM openjdk:21
EXPOSE 8081
ADD target/*.jar TaskScheduler.jar
ENTRYPOINT [ "java", "-jar", "TaskScheduler.jar" ]


