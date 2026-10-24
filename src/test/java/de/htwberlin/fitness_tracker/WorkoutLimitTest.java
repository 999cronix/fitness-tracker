package de.htwberlin.fitness_tracker;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/** Prueft die Grenzen, die Server und Datenbank schuetzen: Antwortgroesse und Gesamtzahl. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WorkoutLimitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutEntryRepository repository;

    private void fuelleDatenbank(int anzahl) {
        repository.deleteAll();
        List<WorkoutEntry> eintraege = new ArrayList<>();
        for (int i = 0; i < anzahl; i++) {
            eintraege.add(new WorkoutEntry(null, LocalDate.of(2026, 1, 1), "Squat", 80.0, 5));
        }
        repository.saveAll(eintraege);
    }

    @Test
    void getLiefertHoechstensDieNeuestenFuenfhundertEintraege() throws Exception {
        fuelleDatenbank(WorkoutEntryController.MAX_ENTRIES_PER_RESPONSE + 50);
        mockMvc.perform(get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(WorkoutEntryController.MAX_ENTRIES_PER_RESPONSE)));
    }

    @Test
    void postIstBeiVollerDatenbankMit429Abgelehnt() throws Exception {
        fuelleDatenbank((int) WorkoutEntryController.MAX_ENTRIES_TOTAL);
        mockMvc.perform(post("/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"exercise\":\"Squat\",\"date\":\"2026-10-05\",\"weightKg\":60,\"reps\":8}"))
                .andExpect(status().isTooManyRequests());
    }
}
