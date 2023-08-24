package com.gendra.suggests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuggestionsController.class)
public class SuggestsRestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TermFrequencyCalculator termFrequencyCalculator;

    @MockBean
    private CosineSimilarityCalculator cosineSimilarityCalculator;

    @MockBean
    private HaversineDistanceCalculator haversineDistanceCalculator;
/*
    @BeforeEach
    public void setup() {
        // Configure mock behavior as needed
        when(termFrequencyCalculator.calculateTermFrequencyMap(anyString()))
            .thenReturn(Map.of("cityName", Map.of('c', 1, 'i', 1, 't', 1, 'y', 1, 'n', 1)));

        // Configure other mocks similarly
    }
*/
    @Test
    void testSuggestionsEndpointForLondon() throws Exception {
        mockMvc.perform(get("/suggestions")
                .param("q", "london")
                .param("lat", "42.98339")
                .param("lon", "-81.23304"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("london : America/Toronto"))
                .andExpect(jsonPath("$[0].latitude").value(42.98339))
                .andExpect(jsonPath("$[0].longitude").value(-81.23304))
                .andExpect(jsonPath("$[0].score").value(1.0))
                .andExpect(jsonPath("$[1].name").value("london : America/New_York"))
                .andExpect(jsonPath("$[1].latitude").value(39.88645))
                .andExpect(jsonPath("$[1].longitude").value(-83.44825))
                .andExpect(jsonPath("$[1].score").isNumber())
                .andExpect(jsonPath("$[2].name").value("london : America/New_York"))
                .andExpect(jsonPath("$[2].latitude").isNumber())
                .andExpect(jsonPath("$[2].longitude").isNumber())
                .andExpect(jsonPath("$[2].score").isNumber());
    }

    @Test
    void testSuggestionsEndpointForAjax() throws Exception {
        mockMvc.perform(get("/suggestions")
                .param("q", "ajax")
                .param("lat", "43.85012")
                .param("lon", "-79.03288"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ajax : America/Toronto"))
                .andExpect(jsonPath("$[0].latitude").value(43.85012))
                .andExpect(jsonPath("$[0].longitude").value(-79.03288))
                .andExpect(jsonPath("$[0].score").value(1.0));
    }

    @Test
    void testSuggestionsEndpointForBelAir() throws Exception {
        mockMvc.perform(get("/suggestions")
                .param("q", "bel air")
                .param("lat", "39.53594")
                .param("lon", "-76.34829"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("bel air : America/New_York"))
                .andExpect(jsonPath("$[0].latitude").value(39.53594))
                .andExpect(jsonPath("$[0].longitude").value(-76.34829))
                .andExpect(jsonPath("$[0].score").value(1.0));
    }
    
    @Test
    void testSuggestionsEndpointForKingston() throws Exception {
        mockMvc.perform(get("/suggestions")
                .param("q", "kingston")
                .param("lat", "39.53594")
                .param("lon", "-76.34829"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("kingston : America/New_York"))
                .andExpect(jsonPath("$[0].latitude").value(41.26175))
                .andExpect(jsonPath("$[0].longitude").value(-75.89686))
                .andExpect(jsonPath("$[0].score").value(0.0050846367523284435))
                .andExpect(jsonPath("$[1].name").value("kingston : America/New_York"))
                .andExpect(jsonPath("$[1].latitude").value(41.92704))
                .andExpect(jsonPath("$[1].longitude").value(-73.99736))
                .andExpect(jsonPath("$[1].score").value(0.0030072675608596097))
                .andExpect(jsonPath("$[2].name").value("kingston : America/New_York"))
                .andExpect(jsonPath("$[2].latitude").value(41.48038))
                .andExpect(jsonPath("$[2].longitude").value(-71.52256))
                .andExpect(jsonPath("$[2].score").value(0.0021615450480534006))
                .andExpect(jsonPath("$[3].name").value("kingston : America/Toronto"))
                .andExpect(jsonPath("$[3].latitude").value(44.22976))
                .andExpect(jsonPath("$[3].longitude").value(-76.48098))
                .andExpect(jsonPath("$[3].score").value(0.00191188381984365))
                .andExpect(jsonPath("$[4].name").value("kingston : America/New_York"))
                .andExpect(jsonPath("$[4].latitude").value(41.99455))
                .andExpect(jsonPath("$[4].longitude").value(-70.72448))
                .andExpect(jsonPath("$[4].score").value(0.001825840170479987))
                .andExpect(jsonPath("$[5].name").value("kingston : America/New_York"))
                .andExpect(jsonPath("$[5].latitude").value(42.93648))
                .andExpect(jsonPath("$[5].longitude").value(-71.05339))
                .andExpect(jsonPath("$[5].score").value(0.0017151886063260606))
                .andExpect(jsonPath("$[6].name").value("kingston : America/New_York"))
                .andExpect(jsonPath("$[6].latitude").value(35.88091))
                .andExpect(jsonPath("$[6].longitude").value(-84.50854))
                .andExpect(jsonPath("$[6].score").value(0.001211491565676142));
    }

    void testSuggestionsEndpointForLyndon() throws Exception {
        mockMvc.perform(get("/suggestions")
                .param("q", "lyndon")
                .param("lat", "39.53594")
                .param("lon", "-76.34829"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("lyndon : America/New_York"))
                .andExpect(jsonPath("$[0].latitude").value(44.51422))
                .andExpect(jsonPath("$[0].longitude").value(-72.01093))
                .andExpect(jsonPath("$[0].score").value(0.0015148020855288965))
                .andExpect(jsonPath("$[1].name").value("lyndon : America/Kentucky/Louisville"))
                .andExpect(jsonPath("$[1].latitude").value(38.25674))
                .andExpect(jsonPath("$[1].longitude").value(-85.60163))
                .andExpect(jsonPath("$[1].score").value(0.0012285879276251078));
    }
    @Test
    void testSuggestionsEndpointForBerlin() throws Exception {
        mockMvc.perform(get("/suggestions")
                .param("q", "berlin")
                .param("lat", "39.53594")
                .param("lon", "-76.34829"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("berlin : America/New_York"))
                .andExpect(jsonPath("$[0].latitude").value(39.79123))
                .andExpect(jsonPath("$[0].longitude").value(-74.92905))
                .andExpect(jsonPath("$[0].score").value(0.007951926836352869))
                .andExpect(jsonPath("$[1].name").value("berlin : America/New_York"))
                .andExpect(jsonPath("$[1].latitude").value(44.46867))
                .andExpect(jsonPath("$[1].longitude").value(-71.18508))
                .andExpect(jsonPath("$[1].score").value(0.0014376698577134534))
                .andExpect(jsonPath("$[2].name").value("berlin : America/Chicago"))
                .andExpect(jsonPath("$[2].latitude").value(43.96804))
                .andExpect(jsonPath("$[2].longitude").value(-88.94345))
                .andExpect(jsonPath("$[2].score").value(8.661122075475197E-4));
    }
}
