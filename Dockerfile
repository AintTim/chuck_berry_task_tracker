FROM gradle:8.12.0-jdk21-alpine AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src src
RUN gradle build -x test --no-daemon

FROM tomcat:10.1.26-jre21
COPY --from=builder /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

