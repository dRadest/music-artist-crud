package com.dradest.music.artist.crud.ui.views;

import com.dradest.music.artist.crud.jpa.model.Country;
import com.dradest.music.artist.crud.jpa.persistence.CrudService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "Countries", layout = MainLayout.class)
@PageTitle("Countries")
public class ListView extends VerticalLayout {
    private Grid<Country> grid = new Grid<>(Country.class);
    private TextField filterText = new TextField();

    private CountryForm form;
    private final CrudService service;

    public ListView(CrudService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("country-grid");
        grid.setSizeFull();
        grid.setColumns("name", "code");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCountry(event.getValue()));
    }

    private void configureForm() {
        form = new CountryForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveCountry);
        form.addDeleteListener(this::deleteCountry);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveCountry(CountryForm.SaveEvent event) {
        service.saveCountry(event.getCountry());
        updateList();
        closeEditor();
    }

    private void deleteCountry(CountryForm.DeleteEvent event) {
        service.deleteCountry(event.getCountry());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCountryButton = new Button("Add country");
        addCountryButton.addClickListener(click -> addCountry());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCountryButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void editCountry(Country country) {
        if (country == null) {
            closeEditor();
        } else {
            form.setCountry(country);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCountry(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addCountry() {
        grid.asSingleSelect().clear();
        editCountry(new Country());
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(service.findAllCountries(filterText.getValue()));
    }
}
