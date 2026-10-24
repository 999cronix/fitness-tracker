package de.htwberlin.fitness_tracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/** Voller Kontext mit H2, damit Validierung, JSON und Repository zusammen geprueft werden. */
@SpringBootTest
@AutoConfigureMockMvc
class WorkoutEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutEntryRepository repository;

    @BeforeEach
    void leereDatenbank() {
        repository.deleteAll();
    }

    private ResultActions postJson(String json) throws Exception {
        return mockMvc.perform(post("/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private static String body(String exercise, String date, String weightKg, String reps) {
        return """
                {"exercise":%s,"date":%s,"weightKg":%s,"reps":%s}
                """.formatted(exercise, date, weightKg, reps);
    }

    @Test
    void postGueltigGibt201MitIdUndSpeichertEintrag() throws Exception {
        postJson(body("\"Bench Press\"", "\"2026-10-05\"", "60.5", "8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.exercise").value("Bench Press"))
                .andExpect(jsonPath("$.date").value("2026-10-05"))
                .andExpect(jsonPath("$.weightKg").value(60.5))
                .andExpect(jsonPath("$.reps").value(8));

        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void postMitHeutigemDatumIstErlaubt() throws Exception {
        postJson(body("\"Squat\"", "\"" + LocalDate.now() + "\"", "80", "5"))
                .andExpect(status().isCreated());
    }

    @Test
    void postGrenzwerteSindErlaubt() throws Exception {
        postJson(body("\"" + "a".repeat(100) + "\"", "\"2026-10-05\"", "0.0", "1"))
                .andExpect(status().isCreated());
        postJson(body("\"Squat\"", "\"2026-10-05\"", "1000.0", "1000"))
                .andExpect(status().isCreated());
    }

    @Test
    void postLeereExerciseGibt400() throws Exception {
        postJson(body("\"\"", "\"2026-10-05\"", "60", "8")).andExpect(status().isBadRequest());
        postJson(body("\"   \"", "\"2026-10-05\"", "60", "8")).andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test
    void postFehlendeExerciseGibt400() throws Exception {
        postJson("{\"date\":\"2026-10-05\",\"weightKg\":60,\"reps\":8}")
                .andExpect(status().isBadRequest());
    }

    @Test
    void postExerciseLaengerAls100ZeichenGibt400() throws Exception {
        postJson(body("\"" + "a".repeat(101) + "\"", "\"2026-10-05\"", "60", "8"))
                .andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test
    void postRepsNullGibt400() throws Exception {
        postJson(body("\"Squat\"", "\"2026-10-05\"", "60", "0")).andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test
    void postRepsUeber1000Gibt400() throws Exception {
        postJson(body("\"Squat\"", "\"2026-10-05\"", "60", "1001")).andExpect(status().isBadRequest());
    }

    @Test
    void postNegativesGewichtGibt400() throws Exception {
        postJson(body("\"Squat\"", "\"2026-10-05\"", "-1", "8")).andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test
    void postGewichtUeber1000Gibt400() throws Exception {
        postJson(body("\"Squat\"", "\"2026-10-05\"", "1000.1", "8")).andExpect(status().isBadRequest());
    }

    @Test
    void postFehlendesDatumGibt400() throws Exception {
        postJson("{\"exercise\":\"Squat\",\"weightKg\":60,\"reps\":8}")
                .andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test
    void postDatumInDerZukunftGibt400() throws Exception {
        // 3 Tage: ein Tag Toleranz wegen Zeitzonen ist erlaubt (siehe META-INF/validation.xml)
        String inDreiTagen = LocalDate.now().plusDays(3).toString();
        postJson(body("\"Squat\"", "\"" + inDreiTagen + "\"", "60", "8")).andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test
    void postDatumMorgenWirdWegenZeitzonenAkzeptiert() throws Exception {
        String morgen = LocalDate.now().plusDays(1).toString();
        postJson(body("\"Squat\"", "\"" + morgen + "\"", "60", "8")).andExpect(status().isCreated());
    }

    @Test
    void postFehlendesGewichtGibt400() throws Exception {
        postJson("{\"exercise\":\"Squat\",\"date\":\"2026-10-05\",\"reps\":8}")
                .andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test
    void postKaputtesJsonGibt400OhneStacktrace() throws Exception {
        postJson("{kaputt")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    void postFalscherTypGibt400OhneStacktrace() throws Exception {
        postJson("{\"exercise\":\"Squat\",\"date\":\"2026-10-05\",\"weightKg\":60,\"reps\":\"abc\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    void postFalschesDatumsformatGibt400() throws Exception {
        postJson("{\"exercise\":\"Squat\",\"date\":\"05.10.2026\",\"weightKg\":60,\"reps\":8}")
                .andExpect(status().isBadRequest());
    }

    @Test
    void postMitgeschickteIdWirdIgnoriert() throws Exception {
        postJson("""
                {"id":999999,"exercise":"Squat","date":"2026-10-05","weightKg":60,"reps":8}
                """)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(not(999999)))
                .andExpect(jsonPath("$.id").value(greaterThan(0)));

        assertThat(repository.existsById(999999L)).isFalse();
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void getLiefertLeereListeOhneEintraege() throws Exception {
        mockMvc.perform(get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getSortiertNachDatumAbsteigendDannIdAbsteigend() throws Exception {
        WorkoutEntry alt = repository.save(new WorkoutEntry(null, LocalDate.of(2026, 10, 1), "Alt", 50.0, 5));
        WorkoutEntry mittelErst = repository.save(new WorkoutEntry(null, LocalDate.of(2026, 10, 5), "MittelErst", 60.0, 8));
        WorkoutEntry mittelZweit = repository.save(new WorkoutEntry(null, LocalDate.of(2026, 10, 5), "MittelZweit", 65.0, 6));
        WorkoutEntry neu = repository.save(new WorkoutEntry(null, LocalDate.of(2026, 10, 9), "Neu", 70.0, 4));

        mockMvc.perform(get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(neu.getId()))
                .andExpect(jsonPath("$[1].id").value(mittelZweit.getId()))
                .andExpect(jsonPath("$[2].id").value(mittelErst.getId()))
                .andExpect(jsonPath("$[3].id").value(alt.getId()));
    }

    @Test
    void getZeigtPerPostAngelegtenEintrag() throws Exception {
        postJson(body("\"Deadlift\"", "\"2026-10-05\"", "100", "3")).andExpect(status().isCreated());

        mockMvc.perform(get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].exercise").value("Deadlift"));
    }
}
