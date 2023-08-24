package com.gendra.suggests;

import java.util.Map;

public class CosineSimilarityCalculator {

    public double calculateCosineSimilarity(Map<String, Map<Character, Integer>> termFrequencyMap1,
                                            Map<String, Map<Character, Integer>> termFrequencyMap2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String term : termFrequencyMap1.keySet()) {
            if (termFrequencyMap2.containsKey(term)) {
                for (char c : termFrequencyMap1.get(term).keySet()) {
                    int freq1 = termFrequencyMap1.get(term).get(c);
                    int freq2 = termFrequencyMap2.get(term).getOrDefault(c, 0);
                    dotProduct += freq1 * freq2;
                    norm1 += freq1 * freq1;
                    norm2 += freq2 * freq2;
                }
            }
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        double cosineSimilarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        return 0.5 * (cosineSimilarity + 1.0); // Ajuste para convertir de [-1, 1] a [0, 1]
    }
}