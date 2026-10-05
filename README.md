# Fitness-Tracker

Web-App zum Zusammentragen und Auswerten von Trainingsdaten. Nutzer tragen Workouts ein (Übung, Gewicht, Wiederholungen). Das Backend wertet die Daten per Formel aus und berechnet daraus die nächste Übung bzw. das nächste Gewicht.

## Idee

1. **Daten zusammentragen**: eigene Workout-Einträge, später auch Daten aus angebundenen Apps
2. **Daten auswerten**: Progress, Trainingsverlauf und Empfehlung für das nächste Gewicht
3. **Ergebnis prüfen und erklären**: eine KI kontrolliert die Berechnung und erklärt sie

## Funktionen

| Bereich | Inhalt | Status |
|---|---|---|
| Workouts | Übung, Gewicht, Wiederholungen, Datum eintragen und anzeigen | Milestone 1: Datenmodell und GET-Endpunkt |
| Auswertung | Progress und nächste Übung per Formel (deterministisch im Backend, per Unit-Test prüfbar) | geplant |
| App-Anbindung | Daten aus Fitness-Apps über APIs (z. B. Strava, Whoop) | geplant |
| KI-Analyse | Auswertung von Daten wie Schlaf und Ernährung, Erklärung der Berechnung | geplant |

## Architektur

- **Backend**: Spring Boot (Java), REST-API mit JSON
- **Frontend**: Vue.js, eigenes Repository (ab Milestone 2)
- **Prinzip**: Formeln laufen im Backend. Die KI berechnet nichts, sie prüft und erklärt nur.

## Stand Milestone 1

- Spring-Boot-Backend mit Entity `WorkoutEntry` (id, date, exercise, weight, reps)
- `GET /workouts` liefert eine Liste von Workouts als JSON

## Starten

Voraussetzung: Java installiert.

    git clone https://github.com/999cronix/fitness-tracker.git
    cd fitness-tracker
    ./gradlew bootRun

Danach im Browser öffnen: http://localhost:8080/workouts

Beispielantwort:

    [{"id":1,"date":"2026-10-05","exercise":"Bench Press","weight":60.0,"reps":8}, ...]

## Team

- 999cronix
