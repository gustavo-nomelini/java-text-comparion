package com.prodbygus.javatextcomparion.presentation.view;

import com.prodbygus.javatextcomparion.application.dto.ComparisonResult;
import com.prodbygus.javatextcomparion.application.service.ComparisonService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * View for displaying comparison history.
 */
@Route(value = "history", layout = MainLayout.class)
@PageTitle("History | Text Comparison")
public class ComparisonHistoryView extends Composite<VerticalLayout> {

    private final ComparisonService comparisonService;

    public ComparisonHistoryView(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;
        initializeUI();
    }

    private void initializeUI() {
        VerticalLayout layout = getContent();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.addClassName("history-view");

        H2 title = new H2("Comparison History");
        title.addClassName("page-title");
        layout.add(title);

        // Create grid
        Grid<ComparisonResult> grid = new Grid<>(ComparisonResult.class, false);
        grid.addClassName("history-grid");
        grid.addColumn(ComparisonResult::documentAName).setHeader("Document A");
        grid.addColumn(ComparisonResult::documentBName).setHeader("Document B");
        grid.addColumn(result -> String.format("%.2f%%", result.correlationIndex() * 100))
                .setHeader("Correlation Index");
        grid.addColumn(result -> result.createdAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .setHeader("Created At");

        // Load data
        List<ComparisonResult> comparisons = comparisonService.getAllComparisons(50, 0);
        grid.setItems(comparisons);

        layout.add(grid);
    }
}

