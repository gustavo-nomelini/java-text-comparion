package com.prodbygus.javatextcomparion.infrastructure.similarity;

import com.prodbygus.javatextcomparion.infrastructure.normalization.EnglishTextNormalizer;
import com.prodbygus.javatextcomparion.infrastructure.parser.PdfTextExtractor;
import com.prodbygus.javatextcomparion.shared.exception.FileParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression tests using project sample PDFs to ensure similarity results vary by document pair.
 */
class ComparisonSampleFilesRegressionTest {

    private PdfTextExtractor pdfTextExtractor;
    private CosineSimilarityCalculator cosineSimilarityCalculator;
    private JaccardSimilarityCalculator jaccardSimilarityCalculator;
    private DefaultCorrelationIndexCalculator correlationIndexCalculator;

    @BeforeEach
    void setUp() {
        EnglishTextNormalizer normalizer = new EnglishTextNormalizer();
        pdfTextExtractor = new PdfTextExtractor();
        cosineSimilarityCalculator = new CosineSimilarityCalculator(normalizer);
        jaccardSimilarityCalculator = new JaccardSimilarityCalculator(normalizer);
        correlationIndexCalculator = new DefaultCorrelationIndexCalculator(0.7, 0.3);
    }

    @Test
    void shouldProduceDifferentCorrelationValuesAcrossDifferentPairs() throws IOException, FileParsingException {
        String javaHistory = extractPdf("texts-to-compare/texto1_java_historia.pdf");
        String javaVariation = extractPdf("texts-to-compare/texto2_variacao_java.pdf");
        String javaNew = extractPdf("texts-to-compare/texto3_java_novo.pdf");
        String gardening = extractPdf("texts-to-compare/texto5_jardinagem.pdf");

        double javaHistoryVsVariation = correlation(javaHistory, javaVariation);
        double javaHistoryVsNew = correlation(javaHistory, javaNew);
        double javaHistoryVsGardening = correlation(javaHistory, gardening);

        assertTrue(javaHistoryVsVariation > javaHistoryVsGardening,
                "Expected Java-vs-Java to be more similar than Java-vs-Gardening");
        assertTrue(javaHistoryVsNew > javaHistoryVsGardening,
                "Expected Java-vs-Java to be more similar than Java-vs-Gardening");

        assertNotEquals(round4(javaHistoryVsVariation), round4(javaHistoryVsNew),
                "Different document pairs should not collapse to the same correlation score");
        assertNotEquals(round4(javaHistoryVsVariation), round4(javaHistoryVsGardening),
                "Different document pairs should not collapse to the same correlation score");
    }

    private String extractPdf(String relativePath) throws IOException, FileParsingException {
        Path path = Path.of(relativePath);
        assertTrue(Files.exists(path), "Missing test sample file: " + relativePath);
        return pdfTextExtractor.extractText(Files.readAllBytes(path));
    }

    private double correlation(String textA, String textB) {
        double cosine = cosineSimilarityCalculator.calculate(textA, textB);
        double jaccard = jaccardSimilarityCalculator.calculate(textA, textB);
        return correlationIndexCalculator.calculate(cosine, jaccard);
    }

    private double round4(double value) {
        return Math.round(value * 10_000.0) / 10_000.0;
    }
}

