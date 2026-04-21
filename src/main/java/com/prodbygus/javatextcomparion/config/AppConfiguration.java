package com.prodbygus.javatextcomparion.config;

import com.prodbygus.javatextcomparion.domain.service.SimilarityMetric;
import com.prodbygus.javatextcomparion.infrastructure.similarity.CosineSimilarityCalculator;
import com.prodbygus.javatextcomparion.infrastructure.similarity.JaccardSimilarityCalculator;
import com.prodbygus.javatextcomparion.infrastructure.normalization.EnglishTextNormalizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Spring configuration for beans.
 */
@Configuration
public class AppConfiguration {

    /**
     * Provide list of all available similarity metrics.
     *
     * @param normalizer the text normalizer
     * @return list of metrics
     */
    @Bean
    public List<SimilarityMetric> similarityMetrics(EnglishTextNormalizer normalizer) {
        return Arrays.asList(
                new CosineSimilarityCalculator(normalizer),
                new JaccardSimilarityCalculator(normalizer)
        );
    }
}

