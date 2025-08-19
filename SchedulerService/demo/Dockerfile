# Use an OpenJDK base image
FROM openjdk:24
EXPOSE 8082
ADD target/*.jar SchedulerService.jar
ENTRYPOINT [ "java", "-jar", "SchedulerService.jar" ]


