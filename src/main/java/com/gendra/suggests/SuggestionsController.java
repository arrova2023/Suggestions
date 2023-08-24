package com.gendra.suggests;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
class SuggestionsController {

    private final TermFrequencyCalculator termFrequencyCalculator = new TermFrequencyCalculator();
    private final CosineSimilarityCalculator cosineSimilarityCalculator = new CosineSimilarityCalculator();
    private final HaversineDistanceCalculator haversineDistanceCalculator = new HaversineDistanceCalculator();

    @GetMapping("/suggestions")
    public List<Map<String, Object>> processSuggestions(@RequestParam String q,
                                                        @RequestParam double lat,
                                                        @RequestParam double lon) {
        // Carga y procesa el archivo TSV
        List<Map<String, Object>> suggestions = new ArrayList<>();

        Map<String, Map<Character, Integer>> termFrequencyMapQ = termFrequencyCalculator.calculateTermFrequencyMap(q.toLowerCase());

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("cities_canada-usa.tsv");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                String cityName = fields[1].toLowerCase(); // Convertir a minúsculas
                String tzValue = fields[17]; // Valor de la columna tz
                Map<String, Map<Character, Integer>> termFrequencyMapCity = termFrequencyCalculator.calculateTermFrequencyMap(cityName);
                double cosineSimilarity = cosineSimilarityCalculator.calculateCosineSimilarity(termFrequencyMapQ, termFrequencyMapCity);

                if (cosineSimilarity > 0) {
                    double cityLat = Double.parseDouble(fields[4]); // Latitud
                    double cityLon = Double.parseDouble(fields[5]); // Longitud

                    double distance = haversineDistanceCalculator.haversineDistance(lat, lon, cityLat, cityLon);
                    double similarityScore = 1.0 / (1.0 + distance); // Fórmula de similitud

                    Map<String, Object> suggestion = new LinkedHashMap<>(); // Usar LinkedHashMap para mantener el orden
                    suggestion.put("name", cityName + " : " + tzValue);
                    suggestion.put("latitude", cityLat);
                    suggestion.put("longitude", cityLon);
                    suggestion.put("score", similarityScore);

                    suggestions.add(suggestion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ordenar la lista de sugerencias por score de mayor a menor
        Collections.sort(suggestions, (s1, s2) -> Double.compare((Double) s2.get("score"), (Double) s1.get("score")));

        return suggestions;
    }
}
