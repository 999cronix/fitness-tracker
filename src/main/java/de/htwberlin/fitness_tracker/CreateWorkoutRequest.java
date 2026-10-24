package de.htwberlin.fitness_tracker;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Die Daten, die das Frontend beim Anlegen eines Trainingseintrags schickt.
 * Es gibt bewusst keine id: die Datenbank vergibt sie selbst.
 * Die Pruef-Regeln (Bean Validation) sorgen dafuer, dass nur sinnvolle Werte gespeichert werden.
 *
 * @param exercise der Name der Uebung, nicht leer, hoechstens 100 Zeichen
 * @param date das Datum des Trainings, nicht in der Zukunft
 * @param weightKg das Gewicht in kg, zwischen 0 und 1000
 * @param reps die Wiederholungen, zwischen 1 und 1000
 */
public record CreateWorkoutRequest(
        @NotBlank @Size(max = 100) String exercise,
        @NotNull @PastOrPresent LocalDate date,
        @DecimalMin("0.0") @DecimalMax("1000.0") double weightKg,
        @Min(1) @Max(1000) int reps) {
}
