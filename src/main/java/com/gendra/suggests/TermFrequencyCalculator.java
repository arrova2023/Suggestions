package com.gendra.suggests;

import java.util.HashMap;
import java.util.Map;

public class TermFrequencyCalculator {

    public Map<String, Map<Character, Integer>> calculateTermFrequencyMap(String text) {
        Map<String, Map<Character, Integer>> termFrequencyMap = new HashMap<>();
        Map<Character, Integer> charFrequency = new HashMap<>();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }

        termFrequencyMap.put(text, charFrequency);
        return termFrequencyMap;
    }
}