package com.prodbygus.javatextcomparion.infrastructure.parser;

import com.prodbygus.javatextcomparion.shared.exception.FileParsingException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Extractor for DOCX files using Apache POI.
 */
public class DocxTextExtractor implements TextExtractor {

    @Override
    public String extractText(byte[] fileContent) throws FileParsingException {
        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(fileContent))) {
            StringBuilder text = new StringBuilder();
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String paragraphText = paragraph.getText();
                if (!paragraphText.trim().isEmpty()) {
                    text.append(paragraphText).append("\n");
                }
            }
            return text.toString().trim();
        } catch (IOException e) {
            throw new FileParsingException("Failed to extract text from DOCX file", e);
        }
    }

    @Override
    public boolean supports(String extension) {
        return extension != null && extension.equalsIgnoreCase("docx");
    }
}

