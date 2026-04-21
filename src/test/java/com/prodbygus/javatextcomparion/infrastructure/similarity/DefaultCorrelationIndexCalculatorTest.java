package com.prodbygus.javatextcomparion.infrastructure.similarity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DefaultCorrelationIndexCalculator.
 */
class DefaultCorrelationIndexCalculatorTest {

    private DefaultCorrelationIndexCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DefaultCorrelationIndexCalculator(0.7, 0.3);
    }

    @Test
    void shouldCalculateWeightedAverage() {
        double cosine = 0.8;
        double jaccard = 0.6;
        double expected = (0.8 * 0.7) + (0.6 * 0.3); // 0.56 + 0.18 = 0.74
        double result = calculator.calculate(cosine, jaccard);
        assertEquals(expected, result, 0.001);
    }

    @Test
    void shouldReturnOneForBothMetricsOne() {
        double result = calculator.calculate(1.0, 1.0);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    void shouldReturnZeroForBothMetricsZero() {
        double result = calculator.calculate(0.0, 0.0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void shouldThrowForInvalidCosineScore() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(1.5, 0.5));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(-0.1, 0.5));
    }

    @Test
    void shouldThrowForInvalidJaccardScore() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(0.5, 1.5));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(0.5, -0.1));
    }

    @Test
    void shouldReturnCorrectWeights() {
        List<Double> weights = calculator.getWeights();
        assertEquals(2, weights.size());
        assertEquals(0.7, weights.get(0));
        assertEquals(0.3, weights.get(1));
    }

    @Test
    void shouldValidateWeightSum() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultCorrelationIndexCalculator(0.5, 0.3));
    }
}

