FROM gradle:8.12.0-jdk21-alpine AS builder

COPY build.gradle settings.gradle /usr/src/app/

COPY src /usr/src/app/src

WORKDIR /usr/src/app

RUN gradle clean build --no-daemon

FROM tomcat:10.1.26-jre21

COPY --from=builder ./chuck_berry_task_tracker-1.0.war /usr/local/tomcat/webapps/task_tracker.war

EXPOSE 8080



