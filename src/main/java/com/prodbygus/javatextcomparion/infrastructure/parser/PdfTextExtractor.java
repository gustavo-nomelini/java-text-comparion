package com.prodbygus.javatextcomparion.infrastructure.parser;

import com.prodbygus.javatextcomparion.shared.exception.FileParsingException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

/**
 * Extractor for PDF files using Apache PDFBox.
 */
public class PdfTextExtractor implements TextExtractor {

    @Override
    public String extractText(byte[] fileContent) throws FileParsingException {
        try (PDDocument document = Loader.loadPDF(fileContent)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        } catch (IOException e) {
            throw new FileParsingException("Failed to extract text from PDF file", e);
        }
    }

    @Override
    public boolean supports(String extension) {
        return extension != null && extension.equalsIgnoreCase("pdf");
    }
}

