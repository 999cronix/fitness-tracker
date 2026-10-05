package de.htwberlin.fitness_tracker;

import java.time.LocalDate;

public class WorkoutEntry {
    private Long id;
    private LocalDate date;
    private String exercise;
    private double weightKg;
    private int reps;

    public WorkoutEntry(Long id, LocalDate date, String exercise, double weightKg, int reps) {
        this.id = id;
        this.date = date;
        this.exercise = exercise;
        this.weightKg = weightKg;
        this.reps = reps;
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getExercise() { return exercise; }
    public double getWeightKg() { return weightKg; }
    public int getReps() { return reps; }
}
