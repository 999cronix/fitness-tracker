package de.htwberlin.fitness_tracker;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkoutEntryController {

    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutEntry>> getWorkouts() {
        List<WorkoutEntry> entries = List.of(
            new WorkoutEntry(1L, LocalDate.of(2026, 10, 5), "Bench Press", 60.0, 8),
            new WorkoutEntry(2L, LocalDate.of(2026, 10, 5), "Squat", 80.0, 5)
        );
        return ResponseEntity.ok(entries);
    }
}
