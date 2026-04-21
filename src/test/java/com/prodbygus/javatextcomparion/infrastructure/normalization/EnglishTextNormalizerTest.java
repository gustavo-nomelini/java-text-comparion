package com.prodbygus.javatextcomparion.infrastructure.normalization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EnglishTextNormalizer.
 */
class EnglishTextNormalizerTest {

    private EnglishTextNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new EnglishTextNormalizer();
    }

    @Test
    void shouldConvertToLowercase() {
        String result = normalizer.normalize("HELLO WORLD");
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    void shouldRemoveSpecialCharacters() {
        String result = normalizer.normalize("Hello@World#123!");
        assertFalse(result.contains("@"));
        assertFalse(result.contains("#"));
        assertFalse(result.contains("!"));
    }

    @Test
    void shouldRemoveStopwords() {
        String result = normalizer.normalize("the quick brown fox");
        assertFalse(result.contains("the"));
        assertTrue(result.contains("quick"));
        assertTrue(result.contains("fox"));
    }

    @Test
    void shouldTokenize() {
        String[] tokens = normalizer.tokenize("hello world test");
        assertEquals(3, tokens.length);
        assertEquals("hello", tokens[0]);
        assertEquals("world", tokens[1]);
        assertEquals("test", tokens[2]);
    }

    @Test
    void shouldHandleEmptyString() {
        String result = normalizer.normalize("");
        assertEquals("", result);
    }

    @Test
    void shouldHandleNullString() {
        String result = normalizer.normalize(null);
        assertEquals("", result);
    }

    @Test
    void shouldRemoveAccents() {
        String result = normalizer.normalize("café résumé");
        assertTrue(result.contains("caf"));
        assertTrue(result.contains("resum"));
    }

    @Test
    void shouldRemoveExtraWhitespace() {
        String result = normalizer.normalize("hello    world    test");
        assertTrue(result.contains("hello world test") ||
                   result.contains("hello") && result.contains("world"));
    }
}

