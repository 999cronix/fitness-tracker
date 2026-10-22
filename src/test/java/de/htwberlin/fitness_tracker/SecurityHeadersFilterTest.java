package de.htwberlin.fitness_tracker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** Prueft, dass GET /workouts die Sicherheits-Header mitschickt. */
class SecurityHeadersFilterTest {

    // Controller und Filter ohne den ganzen Spring-Start, das geht schnell
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new WorkoutEntryController())
            .addFilters(new SecurityHeadersFilter())
            .build();

    @Test
    void workoutsAntwortEnthaeltSicherheitsHeader() throws Exception {
        mockMvc.perform(get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("Referrer-Policy", "no-referrer"));
    }
}
