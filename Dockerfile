FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/api-0.0.1.jar app.jar

RUN mkdir -p /app/data && chmod -R 777 /app/data

ENV DATABASE_URL=jdbc:sqlite:/app/data/bank.db

ENTRYPOINT ["java", "-jar", "app.jar"]