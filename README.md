# Fitness-Tracker (Backend)

Fitness-App, in der Nutzer ihre Workouts eintragen: Übung, Gewicht und Wiederholungen. Aus diesen Daten sollen später automatisch Empfehlungen berechnet werden, zum Beispiel wann das Gewicht erhöht wird. Geplant sind außerdem die Anbindung von Apps über APIs und eine KI, die Daten wie Schlaf und Essen auswertet.

## Stand Milestone 1
- Spring Boot Backend (Java, Gradle)
- Entity-Klasse `WorkoutEntry` mit `id`, `date`, `exercise`, `weightKg`, `reps`
- GET-Route `/workouts` gibt eine Liste von Beispiel-Einträgen als JSON zurück

## Starten
    ./gradlew bootRun
Danach ist die Route unter http://localhost:8080/workouts erreichbar.
