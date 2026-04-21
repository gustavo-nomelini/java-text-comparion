package com.prodbygus.javatextcomparion.infrastructure.similarity;

import com.prodbygus.javatextcomparion.infrastructure.normalization.EnglishTextNormalizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JaccardSimilarityCalculator.
 */
class JaccardSimilarityCalculatorTest {

    private JaccardSimilarityCalculator calculator;
    private EnglishTextNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new EnglishTextNormalizer();
        calculator = new JaccardSimilarityCalculator(normalizer);
    }

    @Test
    void shouldReturnOneForIdenticalTexts() {
        String text = "apple orange banana";
        double similarity = calculator.calculate(text, text);
        assertEquals(1.0, similarity, 0.01);
    }

    @Test
    void shouldReturnZeroForCompletelyDifferentTexts() {
        String text1 = "apple orange banana";
        String text2 = "car truck bus";
        double similarity = calculator.calculate(text1, text2);
        assertEquals(0.0, similarity, 0.01);
    }

    @Test
    void shouldReturnZeroForEmptyTexts() {
        double similarity = calculator.calculate("", "");
        assertEquals(0.0, similarity);
    }

    @Test
    void shouldHandleNullTexts() {
        double similarity = calculator.calculate(null, "text");
        assertEquals(0.0, similarity);
    }

    @Test
    void shouldCalculateSimilarityForPartiallySimilarTexts() {
        String text1 = "apple banana orange grape";
        String text2 = "apple banana cherry date";
        double similarity = calculator.calculate(text1, text2);
        assertTrue(similarity > 0.0 && similarity < 1.0);
        // 2 common tokens (apple, banana), 4 unique total (apple, banana, orange, grape, cherry, date)
        // Jaccard = 2 / 6 = 0.333...
        assertEquals(0.333, similarity, 0.01);
    }

    @Test
    void testGetName() {
        assertTrue(calculator.getName().contains("Jaccard"));
    }
}

