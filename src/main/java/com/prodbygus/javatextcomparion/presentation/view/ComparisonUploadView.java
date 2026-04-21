package com.prodbygus.javatextcomparion.presentation.view;

import com.prodbygus.javatextcomparion.application.dto.ComparisonRequest;
import com.prodbygus.javatextcomparion.application.dto.ComparisonResult;
import com.prodbygus.javatextcomparion.application.dto.DocumentDto;
import com.prodbygus.javatextcomparion.application.dto.DocumentUploadRequest;
import com.prodbygus.javatextcomparion.application.service.ComparisonService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * View for uploading documents and comparing them.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Upload & Compare | Text Comparison")
public class ComparisonUploadView extends Composite<VerticalLayout> {

    private final ComparisonService comparisonService;
    private final List<DocumentDto> uploadedDocuments = new ArrayList<>();

    public ComparisonUploadView(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;
        initializeUI();
    }

    private void initializeUI() {
        VerticalLayout layout = getContent();
        layout.setPadding(true);
        layout.setSpacing(true);

        H2 title = new H2("Upload & Compare Documents");
        layout.add(title);

        // Upload section
        Div uploadSection = createUploadSection();
        layout.add(uploadSection);

        // Comparison section
        Div comparisonSection = createComparisonSection();
        layout.add(comparisonSection);
    }

    private Div createUploadSection() {
        Div section = new Div();
        section.getStyle().set("border", "1px solid #ccc");
        section.getStyle().set("border-radius", "4px");
        section.getStyle().set("padding", "1rem");

        H2 heading = new H2("Step 1: Upload Documents");
        Paragraph info = new Paragraph("Upload TXT, PDF, or DOCX files");

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".txt", ".pdf", ".docx");
        upload.setMaxFiles(2);
        upload.setMaxFileSize(10 * 1024 * 1024); // 10MB

        upload.addSucceededListener(event -> {
            try {
                byte[] data = buffer.getInputStream().readAllBytes();
                String content = new String(data, StandardCharsets.UTF_8);
                String filename = event.getFileName();

                DocumentUploadRequest request = new DocumentUploadRequest(filename, content);
                DocumentDto uploadedDoc = comparisonService.uploadDocument(request);
                uploadedDocuments.add(uploadedDoc);

                Notification.show("Document '" + filename + "' uploaded successfully!", 3000, Notification.Position.TOP_CENTER);

            } catch (IOException e) {
                Notification.show("Error uploading file: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });

        upload.addFailedListener(event ->
                Notification.show("Upload failed: " + event.getReason(), 3000, Notification.Position.TOP_CENTER)
        );

        section.add(heading, info, upload);
        return section;
    }

    private Div createComparisonSection() {
        Div section = new Div();
        section.getStyle().set("border", "1px solid #ccc");
        section.getStyle().set("border-radius", "4px");
        section.getStyle().set("padding", "1rem");
        section.getStyle().set("margin-top", "1rem");

        H2 heading = new H2("Step 2: Compare");
        Paragraph info = new Paragraph("Select two uploaded documents to compare");

        Button compareButton = new Button("Compare Documents", VaadinIcon.PLAY.create());
        compareButton.addClickListener(event -> {
            if (uploadedDocuments.size() < 2) {
                Notification.show("Please upload at least 2 documents", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            compareDocuments();
        });

        section.add(heading, info, compareButton);
        return section;
    }

    private void compareDocuments() {
        if (uploadedDocuments.size() < 2) {
            return;
        }

        DocumentDto doc1 = uploadedDocuments.get(0);
        DocumentDto doc2 = uploadedDocuments.get(1);

        ComparisonRequest request = new ComparisonRequest(doc1.id(), doc2.id());

        try {
            ComparisonResult result = comparisonService.compareDocuments(request);
            showResultsDialog(result);
        } catch (Exception e) {
            Notification.show("Error comparing documents: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void showResultsDialog(ComparisonResult result) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Comparison Results");
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        Paragraph summary = new Paragraph(result.summary());
        Div metrics = new Div();
        metrics.getStyle().set("display", "flex");
        metrics.getStyle().set("gap", "1rem");
        metrics.add(
                createMetricCard("Correlation Index", String.format("%.2f%%", result.correlationIndex() * 100)),
                createMetricCard("Cosine Similarity", String.format("%.2f%%", result.cosineSimilarity() * 100)),
                createMetricCard("Jaccard Similarity", String.format("%.2f%%", result.jaccardSimilarity() * 100))
        );

        content.add(summary, metrics);
        dialog.add(content);

        Button closeButton = new Button("Close", event -> dialog.close());
        dialog.getFooter().add(closeButton);

        dialog.open();
    }

    private Div createMetricCard(String label, String value) {
        Div card = new Div();
        card.getStyle().set("border", "1px solid #ccc");
        card.getStyle().set("border-radius", "4px");
        card.getStyle().set("padding", "1rem");
        card.getStyle().set("flex", "1");

        H2 metricValue = new H2(value);
        metricValue.getStyle().set("margin", "0");
        metricValue.getStyle().set("font-size", "2em");

        Paragraph metricLabel = new Paragraph(label);
        metricLabel.getStyle().set("margin", "0.5rem 0 0 0");
        metricLabel.getStyle().set("color", "#888");

        card.add(metricValue, metricLabel);
        return card;
    }
}

