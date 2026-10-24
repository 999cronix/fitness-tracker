package de.htwberlin.fitness_tracker;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Schnittstelle fuer Trainingseintraege. GET /workouts liefert alle Eintraege,
 * POST /workouts speichert einen neuen Eintrag in der Datenbank.
 */
@RestController
public class WorkoutEntryController {

    private final WorkoutEntryRepository repository;

    /**
     * Erhaelt das Repository von Spring, ueber das die Datenbank angesprochen wird.
     *
     * @param repository die Datenbank-Schnittstelle fuer Trainingseintraege
     */
    public WorkoutEntryController(WorkoutEntryRepository repository) {
        this.repository = repository;
    }

    /**
     * Liefert alle Trainingseintraege. Neueste zuerst: erst nach Datum absteigend,
     * bei gleichem Datum nach id absteigend (der zuletzt angelegte steht oben).
     *
     * @return die Liste der Eintraege mit Status 200
     */
    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutEntry>> getWorkouts() {
        List<WorkoutEntry> entries = repository.findAll(
                Sort.by(Sort.Direction.DESC, "date", "id"));
        return ResponseEntity.ok(entries);
    }

    /**
     * Legt einen neuen Trainingseintrag an. Die Pruefung der Eingabe passiert vorher
     * ueber @Valid; ungueltige Daten werden mit Status 400 abgelehnt.
     *
     * @param request die geprueften Daten aus dem Request-Body
     * @return der gespeicherte Eintrag (mit neuer id) mit Status 201
     */
    @PostMapping("/workouts")
    public ResponseEntity<WorkoutEntry> createWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
        // id ist null, damit die Datenbank eine neue id vergibt
        WorkoutEntry entry = new WorkoutEntry(
                null, request.date(), request.exercise(), request.weightKg(), request.reps());
        WorkoutEntry saved = repository.save(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
