package de.htwberlin.fitness_tracker;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Schnittstelle fuer Trainingseintraege. GET /workouts liefert die neuesten Eintraege,
 * POST /workouts speichert einen neuen Eintrag in der Datenbank.
 * Zwei Grenzen schuetzen den Server und die kleine Datenbank: eine Antwort enthaelt
 * hoechstens MAX_ENTRIES_PER_RESPONSE Eintraege, und es werden hoechstens MAX_ENTRIES_TOTAL gespeichert.
 */
@RestController
public class WorkoutEntryController {

    /** So viele Eintraege liefert GET /workouts hoechstens auf einmal (die neuesten). */
    static final int MAX_ENTRIES_PER_RESPONSE = 500;

    /** So viele Eintraege duerfen insgesamt gespeichert werden. Danach antwortet POST mit 429. */
    static final long MAX_ENTRIES_TOTAL = 10_000;

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
     * Liefert die neuesten Trainingseintraege, hoechstens MAX_ENTRIES_PER_RESPONSE.
     * Neueste zuerst: erst nach Datum absteigend, bei gleichem Datum nach id absteigend
     * (der zuletzt angelegte steht oben).
     *
     * @return die Liste der Eintraege mit Status 200
     */
    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutEntry>> getWorkouts() {
        // PageRequest holt nur die erste Seite mit MAX_ENTRIES_PER_RESPONSE Eintraegen
        List<WorkoutEntry> entries = repository.findAll(PageRequest.of(
                0, MAX_ENTRIES_PER_RESPONSE, Sort.by(Sort.Direction.DESC, "date", "id"))).getContent();
        return ResponseEntity.ok(entries);
    }

    /**
     * Legt einen neuen Trainingseintrag an. Die Pruefung der Eingabe passiert vorher
     * ueber @Valid; ungueltige Daten werden mit Status 400 abgelehnt.
     *
     * @param request die geprueften Daten aus dem Request-Body
     * @return der gespeicherte Eintrag (mit neuer id) mit Status 201, oder 429, wenn die Datenbank voll ist
     */
    @PostMapping("/workouts")
    public ResponseEntity<WorkoutEntry> createWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
        if (repository.count() >= MAX_ENTRIES_TOTAL) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        }
        // id ist null, damit die Datenbank eine neue id vergibt
        WorkoutEntry entry = new WorkoutEntry(
                null, request.date(), request.exercise(), request.weightKg(), request.reps());
        WorkoutEntry saved = repository.save(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
