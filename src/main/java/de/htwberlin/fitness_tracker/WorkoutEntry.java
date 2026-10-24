package de.htwberlin.fitness_tracker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

/**
 * Ein einzelner Trainingseintrag, z. B. Bankdruecken mit 60 kg und 8 Wiederholungen.
 * Die Klasse ist eine JPA-Entity. JPA (Java Persistence API) legt Java-Objekte
 * in einer Datenbank-Tabelle ab und holt sie von dort wieder.
 */
@Entity
public class WorkoutEntry {

    // Die Datenbank vergibt die id beim Speichern selbst (fortlaufende Nummer)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Die Spalte heisst workout_date, weil "date" in SQL ein Schluesselwort ist
    @Column(name = "workout_date")
    private LocalDate date;
    private String exercise;
    private double weightKg;
    private int reps;

    /**
     * Erstellt einen Trainingseintrag mit allen Werten.
     * Die id ist null, wenn der Eintrag noch nicht gespeichert wurde.
     *
     * @param id die Nummer des Eintrags, oder null vor dem Speichern
     * @param date das Datum des Trainings
     * @param exercise der Name der Uebung
     * @param weightKg das Gewicht in Kilogramm
     * @param reps die Anzahl der Wiederholungen
     */
    public WorkoutEntry(Long id, LocalDate date, String exercise, double weightKg, int reps) {
        this.id = id;
        this.date = date;
        this.exercise = exercise;
        this.weightKg = weightKg;
        this.reps = reps;
    }

    /**
     * Leerer Konstruktor nur fuer JPA. JPA braucht ihn, um Eintraege aus der Datenbank zu lesen.
     */
    protected WorkoutEntry() {
    }

    /**
     * Gibt die Nummer des Eintrags zurueck.
     *
     * @return die id, oder null, wenn der Eintrag noch nicht gespeichert ist
     */
    public Long getId() { return id; }

    /**
     * Gibt das Datum des Trainings zurueck.
     *
     * @return das Datum
     */
    public LocalDate getDate() { return date; }

    /**
     * Gibt den Namen der Uebung zurueck.
     *
     * @return der Uebungsname
     */
    public String getExercise() { return exercise; }

    /**
     * Gibt das Gewicht in Kilogramm zurueck.
     *
     * @return das Gewicht in kg
     */
    public double getWeightKg() { return weightKg; }

    /**
     * Gibt die Anzahl der Wiederholungen zurueck.
     *
     * @return die Wiederholungen
     */
    public int getReps() { return reps; }
}
