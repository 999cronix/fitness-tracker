# Zweistufiges Dockerfile: Stufe 1 baut die jar, Stufe 2 enthaelt nur die Laufzeitumgebung und die fertige jar.

# Stufe 1: Build mit JDK und Gradle-Wrapper
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
# Zuerst nur die Build-Dateien kopieren und die Abhaengigkeiten laden.
# Diese Schicht bleibt im Docker-Cache, solange sich die Build-Dateien nicht aendern.
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
# Gradle-Wrapper ausfuehrbar machen, falls die Rechte beim Kopieren verloren gehen
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null
# Erst danach den Quellcode kopieren, der sich oft aendert
COPY src src
RUN ./gradlew bootJar --no-daemon

# Stufe 2: schlankes Laufzeit-Image nur mit JRE
FROM eclipse-temurin:25-jre
WORKDIR /app
# Nur die ausfuehrbare jar kopieren; die *-plain.jar wird durch das Muster *-SNAPSHOT.jar ausgeschlossen
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar
# Die Anwendung laeuft nicht als root, sondern als normaler Benutzer ohne Sonderrechte
RUN useradd --system --no-create-home appuser
USER appuser
# Dokumentiert den Port, auf dem die Anwendung lauscht (Render setzt zusaetzlich PORT)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
