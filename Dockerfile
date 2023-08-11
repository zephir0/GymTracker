# Użyj oficjalnego obrazu OpenJDK 17 jako obrazu bazowego
FROM openjdk:17-jdk-slim

# Utwórz katalog /app w kontenerze
RUN mkdir /app

# Skopiuj plik JAR z lokalnego katalogu (./app/target) do kontenera (/app)
COPY ./app/target/GymTracker-0.0.1-SNAPSHOT.jar /app/GymTracker-0.0.1-SNAPSHOT.jar

# Ustaw katalog roboczy na /app
WORKDIR /app

# Określ, że kontener nasłuchuje na porcie 8080 (taki, jak używa domyślnie Spring Boot)
EXPOSE 8080

# Uruchom aplikację Spring Boot, gdy kontener zostanie uruchomiony
CMD ["java", "-jar", "GymTracker-0.0.1-SNAPSHOT.jar"]
