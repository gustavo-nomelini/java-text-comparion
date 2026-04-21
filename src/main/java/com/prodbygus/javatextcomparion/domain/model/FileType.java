package com.prodbygus.javatextcomparion.domain.model;

/**
 * Supported file types for document comparison.
 */
public enum FileType {
    TXT,
    PDF,
    DOCX;

    /**
     * Determine file type from filename extension.
     *
     * @param filename the filename
     * @return the FileType
     * @throws IllegalArgumentException if file type is not supported
     */
    public static FileType fromFileName(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Invalid filename: " + filename);
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        return switch (extension) {
            case "txt" -> TXT;
            case "pdf" -> PDF;
            case "docx" -> DOCX;
            default -> throw new IllegalArgumentException("Unsupported file type: " + extension);
        };
    }
}

