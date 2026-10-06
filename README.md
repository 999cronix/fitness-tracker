# Fitness-Tracker

Web-App, die Fitnessdaten an einem Ort zusammenfasst, speichert und auswertet: Gym, Laufen, Schlaf, Kalorien und mehr.

## Projektbeschreibung

Nutzer sammeln ihre Daten rund um Fitness in einer App:

- **Training**: Gym-Workouts (Übung, Gewicht, Wiederholungen) und Läufe
- **Schlaf und Ernährung**: Schlafdauer, Kalorien
- **Supplements**: welche Nahrungsergänzungsmittel eingenommen werden

Die Daten werden dauerhaft gespeichert und ausgewertet. Mehrere Accounts werden unterstützt.

## Auswertung: Formeln und KI

| Teil | Aufgabe |
|---|---|
| **Formeln (Backend)** | Tracking und Berechnungen laufen deterministisch im Backend, z. B. Progress, nächstes Trainingsgewicht und Kalorien. Sie sind per Unit-Test prüfbar. |
| **KI** | Prüft die berechneten Ergebnisse und gibt Hinweise. Grundlage sind Prompts und Wissen (Knowledge), die wir ihr mitgeben. Die KI erklärt die Berechnungen nicht und rechnet nicht selbst. |

## Funktionen

| Bereich | Inhalt | Status |
|---|---|---|
| Workouts | Einträge anlegen und anzeigen | Milestone 1: Datenmodell und GET-Endpunkt |
| Tracking und Berechnung | Progress, nächste Übung, Kalorien per Formel | geplant |
| Accounts | Mehrere Nutzer, Anmeldung | geplant |
| Supplements | Eingabe durch den Nutzer, fließt in die KI-Hinweise ein | geplant |
| App-Anbindung | Daten aus Fitness- und Ernährungs-Apps über APIs (z. B. Strava, Whoop, YAZIO), sofern ein Zugang verfügbar ist | geplant |
| KI-Hinweise | Prüfung der Ergebnisse, Hinweise anhand von Prompts und Knowledge | geplant |

## Architektur

- **Backend**: Spring Boot (Java), REST-API mit JSON
- **Frontend**: Vue.js, eigenes Repository (ab Milestone 2)
- **Datenbank**: PostgreSQL (ab Milestone 4)

## Stand Milestone 1

- Spring-Boot-Backend mit Entity `WorkoutEntry` (id, date, exercise, weight, reps)
- `GET /workouts` liefert eine Liste von Beispiel-Workouts als JSON

## Starten

Voraussetzung: Java installiert.

    git clone https://github.com/999cronix/fitness-tracker.git
    cd fitness-tracker
    ./gradlew bootRun

Danach im Browser öffnen: http://localhost:8080/workouts

## Team

- 999cronix
- JustinM27
