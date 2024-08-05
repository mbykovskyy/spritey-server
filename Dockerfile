FROM maven:3.9.7-eclipse-temurin-22-alpine AS build
ENV APP_DIR=/usr/app
RUN mkdir -p $APP_DIR
WORKDIR $APP_DIR
COPY src src
COPY pom.xml .
RUN mvn -f pom.xml clean package

FROM eclipse-temurin:22-jre-alpine
ARG JAR_FILE=/usr/app/target/spritey-server-*-jar-with-dependencies.jar
COPY --from=build $JAR_FILE /app/server.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/server.jar
