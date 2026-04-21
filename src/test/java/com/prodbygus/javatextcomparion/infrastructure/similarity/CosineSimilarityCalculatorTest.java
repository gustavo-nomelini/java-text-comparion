package com.prodbygus.javatextcomparion.infrastructure.similarity;

import com.prodbygus.javatextcomparion.infrastructure.normalization.EnglishTextNormalizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CosineSimilarityCalculator.
 */
class CosineSimilarityCalculatorTest {

    private CosineSimilarityCalculator calculator;
    private EnglishTextNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new EnglishTextNormalizer();
        calculator = new CosineSimilarityCalculator(normalizer);
    }

    @Test
    void shouldReturnOneForIdenticalTexts() {
        String text = "The quick brown fox jumps over the lazy dog";
        double similarity = calculator.calculate(text, text);
        assertEquals(1.0, similarity, 0.01);
    }

    @Test
    void shouldReturnZeroForCompletlyDifferentTexts() {
        String text1 = "Apple Orange Banana";
        String text2 = "Car Truck Bus";
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
        String text1 = "The cat is sleeping on the couch";
        String text2 = "The dog is sleeping on the mat";
        double similarity = calculator.calculate(text1, text2);
        assertTrue(similarity > 0.0 && similarity < 1.0);
    }

    @Test
    void testGetName() {
        assertTrue(calculator.getName().contains("Cosine"));
    }
}

