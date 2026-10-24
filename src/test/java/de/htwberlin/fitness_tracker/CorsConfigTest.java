package de.htwberlin.fitness_tracker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Prueft die CORS-Regeln: nur das Frontend darf, nur GET, POST und OPTIONS, nur Content-Type. */
@SpringBootTest
@AutoConfigureMockMvc
class CorsConfigTest {

    // Standardwert aus application.properties (app.cors.allowed-origins)
    private static final String FRONTEND = "http://localhost:5173";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void preflightFuerPostVomFrontendIstErlaubt() throws Exception {
        mockMvc.perform(options("/workouts")
                        .header("Origin", FRONTEND)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", FRONTEND));
    }

    @Test
    void preflightVonFremderOriginWirdAbgelehnt() throws Exception {
        mockMvc.perform(options("/workouts")
                        .header("Origin", "http://evil.example")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isForbidden());
    }

    @Test
    void preflightFuerDeleteWirdAbgelehnt() throws Exception {
        mockMvc.perform(options("/workouts")
                        .header("Origin", FRONTEND)
                        .header("Access-Control-Request-Method", "DELETE"))
                .andExpect(status().isForbidden());
    }

    @Test
    void preflightMitAnderemHeaderWirdAbgelehnt() throws Exception {
        mockMvc.perform(options("/workouts")
                        .header("Origin", FRONTEND)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isForbidden());
    }

    @Test
    void postVomFrontendBekommtDenAllowOriginHeader() throws Exception {
        mockMvc.perform(post("/workouts")
                        .header("Origin", FRONTEND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"exercise\":\"Squat\",\"date\":\"2026-10-24\",\"weightKg\":80,\"reps\":5}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Access-Control-Allow-Origin", FRONTEND));
    }

    @Test
    void postVonFremderOriginWirdAbgelehnt() throws Exception {
        mockMvc.perform(post("/workouts")
                        .header("Origin", "http://evil.example")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"exercise\":\"Squat\",\"date\":\"2026-10-24\",\"weightKg\":80,\"reps\":5}"))
                .andExpect(status().isForbidden());
    }
}
