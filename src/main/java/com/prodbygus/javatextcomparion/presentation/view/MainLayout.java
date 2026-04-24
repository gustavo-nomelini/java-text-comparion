package com.prodbygus.javatextcomparion.presentation.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

/**
 * Main application layout with navigation.
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 title = new H1("Text Comparison System");
        title.addClassName("app-title");

        var toggle = new DrawerToggle();

        addToNavbar(toggle, title);
    }

    private void createDrawer() {
        var nav = new SideNav();
        nav.addClassName("app-side-nav");

        var uploadItem = new SideNavItem("Upload & Compare", ComparisonUploadView.class);
        var historyItem = new SideNavItem("History", ComparisonHistoryView.class);

        nav.addItem(uploadItem);
        nav.addItem(historyItem);

        addToDrawer(nav);
    }
}

