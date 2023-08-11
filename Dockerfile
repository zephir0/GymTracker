FROM openjdk:17-jdk-slim

RUN mkdir /app

COPY ./target/GymTracker-0.0.1-SNAPSHOT.jar /app/GymTracker-0.0.1-SNAPSHOT.jar

WORKDIR /app

EXPOSE 20183

CMD ["java", "-jar", "GymTracker-0.0.1-SNAPSHOT.jar"]
