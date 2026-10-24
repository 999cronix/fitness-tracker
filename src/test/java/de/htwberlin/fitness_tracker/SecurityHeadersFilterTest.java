package de.htwberlin.fitness_tracker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** Prueft, dass GET /workouts die Sicherheits-Header mitschickt. */
class SecurityHeadersFilterTest {

    // Scheinobjekt fuer das Repository, damit kein Datenbank-Start noetig ist
    private final WorkoutEntryRepository repository = mock(WorkoutEntryRepository.class);

    // Controller und Filter ohne den ganzen Spring-Start, das geht schnell
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new WorkoutEntryController(repository))
            .addFilters(new SecurityHeadersFilter())
            .build();

    @BeforeEach
    void repositoryLiefertLeereListe() {
        // Die Sortier-Abfrage im Controller bekommt eine leere Liste zurueck
        when(repository.findAll(any(Sort.class))).thenReturn(List.of());
    }

    @Test
    void workoutsAntwortEnthaeltSicherheitsHeader() throws Exception {
        mockMvc.perform(get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("Referrer-Policy", "no-referrer"));
    }
}
