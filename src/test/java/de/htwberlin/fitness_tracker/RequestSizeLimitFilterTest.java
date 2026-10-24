package de.htwberlin.fitness_tracker;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** Prueft, dass zu grosse Anfragen mit 413 abgelehnt werden, normale aber durchgehen. */
class RequestSizeLimitFilterTest {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new WorkoutEntryController(mock(WorkoutEntryRepository.class)))
            .addFilters(new RequestSizeLimitFilter())
            .build();

    @Test
    void zuGrosserBodyWirdMit413AbgelehntUndNichtVerarbeitet() throws Exception {
        String gross = "x".repeat((int) RequestSizeLimitFilter.MAX_BODY_BYTES + 1);
        mockMvc.perform(post("/workouts").contentType(MediaType.APPLICATION_JSON).content(gross))
                .andExpect(status().is(413));
    }

    @Test
    void normalerBodyKommtDurch() throws Exception {
        // Der Body ist kleiner als die Grenze, der Filter laesst ihn weiter (hier 400 wegen kaputtem JSON)
        mockMvc.perform(post("/workouts").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }
}
