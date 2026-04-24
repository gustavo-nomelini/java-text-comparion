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
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * View for uploading documents and comparing them.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Upload & Compare | Text Comparison")
public class ComparisonUploadView extends Composite<VerticalLayout> {

    private static final int MAX_COMPARISON_DOCS = 2;

    private final ComparisonService comparisonService;
    private final Deque<DocumentDto> documentsForComparison = new ArrayDeque<>(MAX_COMPARISON_DOCS);
    private final Paragraph selectedDocumentsInfo = new Paragraph("Nenhum documento pronto para comparacao.");
    private final Paragraph lastComparisonInfo = new Paragraph("Nenhuma comparacao executada ainda.");
    private Button compareButton;

    public ComparisonUploadView(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;
        initializeUI();
    }

    private void initializeUI() {
        VerticalLayout layout = getContent();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.addClassName("upload-view");

        H2 title = new H2("Upload & Compare Documents");
        title.addClassName("page-title");
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
        section.addClassName("section-card");

        H2 heading = new H2("Step 1: Upload Documents");
        heading.addClassName("section-title");
        Paragraph info = new Paragraph("Upload TXT, PDF, or DOCX files");
        info.addClassName("section-description");

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload();
        upload.setReceiver(buffer);
        upload.addClassName("upload-dropzone");
        upload.setAcceptedFileTypes(".txt", ".pdf", ".docx");
        upload.setMaxFiles(2);
        upload.setMaxFileSize(10 * 1024 * 1024); // 10MB

        upload.addSucceededListener(event -> {
            try {
                String filename = event.getFileName();

                byte[] data = buffer.getInputStream(filename).readAllBytes();

                DocumentUploadRequest request = new DocumentUploadRequest(filename, data);
                DocumentDto uploadedDoc = comparisonService.uploadDocument(request);
                addDocumentForComparison(uploadedDoc);
                updateComparisonState();

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
        section.addClassName("section-card");
        section.addClassName("comparison-card");

        H2 heading = new H2("Step 2: Compare");
        heading.addClassName("section-title");
        Paragraph info = new Paragraph("Select two uploaded documents to compare");
        info.addClassName("section-description");

        selectedDocumentsInfo.addClassName("section-description");
        lastComparisonInfo.addClassName("section-description");

        compareButton = new Button("Compare Documents", VaadinIcon.PLAY.create());
        compareButton.addClassName("primary-action");
        compareButton.setEnabled(false);
        compareButton.addClickListener(event -> {
            if (documentsForComparison.size() < MAX_COMPARISON_DOCS) {
                Notification.show("Please upload at least 2 documents", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            compareDocuments();
        });

        section.add(heading, info, selectedDocumentsInfo, lastComparisonInfo, compareButton);
        return section;
    }

    private void compareDocuments() {
        if (documentsForComparison.size() < MAX_COMPARISON_DOCS) {
            return;
        }

        List<DocumentDto> selectedDocuments = new ArrayList<>(documentsForComparison);
        DocumentDto doc1 = selectedDocuments.get(0);
        DocumentDto doc2 = selectedDocuments.get(1);

        ComparisonRequest request = new ComparisonRequest(doc1.id(), doc2.id());

        try {
            ComparisonResult result = comparisonService.compareDocuments(request);
            lastComparisonInfo.setText(String.format(
                    "Ultimo resultado: %s x %s => Indice de correlacao %.2f%%",
                    result.documentAName(),
                    result.documentBName(),
                    result.correlationIndex() * 100
            ));
            showResultsDialog(result);
        } catch (Exception e) {
            Notification.show("Error comparing documents: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void addDocumentForComparison(DocumentDto document) {
        if (documentsForComparison.size() == MAX_COMPARISON_DOCS) {
            documentsForComparison.removeFirst();
        }
        documentsForComparison.addLast(document);
    }

    private void updateComparisonState() {
        compareButton.setEnabled(documentsForComparison.size() == MAX_COMPARISON_DOCS);

        if (documentsForComparison.isEmpty()) {
            selectedDocumentsInfo.setText("Nenhum documento pronto para comparacao.");
            return;
        }

        List<String> fileNames = documentsForComparison.stream()
                .map(DocumentDto::originalFileName)
                .toList();

        selectedDocumentsInfo.setText("Documentos selecionados: " + String.join(" x ", fileNames));
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
        summary.addClassName("result-summary");
        Div metrics = new Div();
        metrics.addClassName("metrics-grid");
        metrics.add(
                createMetricCard("Correlation Index", String.format("%.2f%%", result.correlationIndex() * 100)),
                createMetricCard("Cosine Similarity", String.format("%.2f%%", result.cosineSimilarity() * 100)),
                createMetricCard("Jaccard Similarity", String.format("%.2f%%", result.jaccardSimilarity() * 100))
        );

        content.add(summary, metrics);
        dialog.add(content);

        Button closeButton = new Button("Close", event -> dialog.close());
        closeButton.addClassName("secondary-action");
        dialog.getFooter().add(closeButton);

        dialog.open();
    }

    private Div createMetricCard(String label, String value) {
        Div card = new Div();
        card.addClassName("metric-card");

        H2 metricValue = new H2(value);
        metricValue.addClassName("metric-value");

        Paragraph metricLabel = new Paragraph(label);
        metricLabel.addClassName("metric-label");

        card.add(metricValue, metricLabel);
        return card;
    }
}

