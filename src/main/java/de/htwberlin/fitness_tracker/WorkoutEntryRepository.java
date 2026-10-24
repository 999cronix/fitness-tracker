package de.htwberlin.fitness_tracker;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository fuer Trainingseintraege. Spring erstellt die Klasse zur Laufzeit selbst,
 * sie bietet fertige Methoden wie speichern (save), laden (findById) und alle holen (findAll).
 */
public interface WorkoutEntryRepository extends JpaRepository<WorkoutEntry, Long> {
}
