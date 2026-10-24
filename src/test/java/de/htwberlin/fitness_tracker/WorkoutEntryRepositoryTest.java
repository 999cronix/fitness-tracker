package de.htwberlin.fitness_tracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class WorkoutEntryRepositoryTest {

    @Autowired
    private WorkoutEntryRepository repository;

    @Test
    void speichertEintragUndLaedtIhnMitGeneriertemId() {
        WorkoutEntry saved = repository.save(
                new WorkoutEntry(null, LocalDate.of(2026, 10, 5), "Bench Press", 60.5, 8));

        assertThat(saved.getId()).isNotNull();

        WorkoutEntry found = repository.findById(saved.getId()).orElseThrow();
        assertThat(found.getExercise()).isEqualTo("Bench Press");
        assertThat(found.getDate()).isEqualTo(LocalDate.of(2026, 10, 5));
        assertThat(found.getWeightKg()).isEqualTo(60.5);
        assertThat(found.getReps()).isEqualTo(8);
    }

    @Test
    void findAllLiefertAlleGespeichertenEintraege() {
        repository.save(new WorkoutEntry(null, LocalDate.of(2026, 10, 5), "Squat", 80.0, 5));
        repository.save(new WorkoutEntry(null, LocalDate.of(2026, 10, 6), "Deadlift", 100.0, 3));

        assertThat(repository.findAll()).hasSize(2);
    }
}
