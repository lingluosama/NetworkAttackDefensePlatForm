FROM openjdk:17-jdk-slim
LABEL authors="Ling_luo"

WORKDIR /app

COPY target/AttackDefendPlatform-0.0.1-SNAPSHOT.jar /app

EXPOSE 8990

ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar","AttackDefendPlatform-0.0.1-SNAPSHOT.jar"]