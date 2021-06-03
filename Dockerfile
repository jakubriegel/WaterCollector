FROM maven:3.8.1-openjdk-16 as setup-stage

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY src /app/src

RUN mvn package spring-boot:repackage

FROM openjdk:16 as develop-stage

COPY --from=setup-stage /app/target/WaterCollector.jar /WaterCollector.jar
COPY deploy/application-dev.properties /application.properties

EXPOSE 80

CMD java -jar /WaterCollector.jar

FROM openjdk:16 as production-stage

COPY --from=setup-stage /app/target/WaterCollector.jar /WaterCollector.jar
COPY deploy/application-prod.properties /application.properties

EXPOSE 80

CMD java -jar /WaterCollector.jar
