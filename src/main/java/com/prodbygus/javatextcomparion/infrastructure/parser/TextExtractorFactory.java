package com.prodbygus.javatextcomparion.infrastructure.parser;

import com.prodbygus.javatextcomparion.shared.exception.FileParsingException;
import com.prodbygus.javatextcomparion.shared.exception.UnsupportedFileTypeException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Factory for creating appropriate text extractors based on file type.
 */
@Component
public class TextExtractorFactory {

    private final List<TextExtractor> extractors;

    public TextExtractorFactory() {
        this.extractors = Arrays.asList(
                new TxtTextExtractor(),
                new PdfTextExtractor(),
                new DocxTextExtractor()
        );
    }

    /**
     * Get the appropriate extractor for the given file extension.
     *
     * @param filename the filename
     * @return the TextExtractor
     * @throws UnsupportedFileTypeException if no extractor supports the format
     */
    public TextExtractor getExtractor(String filename) throws UnsupportedFileTypeException {
        String extension = getFileExtension(filename);

        return extractors.stream()
                .filter(extractor -> extractor.supports(extension))
                .findFirst()
                .orElseThrow(() -> new UnsupportedFileTypeException(
                        "Unsupported file format: " + extension + ". Supported formats: TXT, PDF, DOCX"
                ));
    }

    /**
     * Extract file extension from filename.
     *
     * @param filename the filename
     * @return the extension (lowercase, without dot)
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}

