package com.dradest.music.artist.crud.ui.views.band;

import com.dradest.music.artist.crud.jpa.model.Band;
import com.dradest.music.artist.crud.jpa.persistence.CrudService;
import com.dradest.music.artist.crud.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "bands", layout = MainLayout.class)
@PageTitle("Bands")
public class BandView extends VerticalLayout {
    private Grid<Band> grid = new Grid<>(Band.class);
    private TextField filterText = new TextField();

    private BandForm form;
    private final CrudService service;

    public BandView(CrudService service) {
        this.service = service;
        addClassName("band-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("band-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editBand(event.getValue()));
    }

    private void configureForm() {
        form = new BandForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveBand);
        form.addDeleteListener(this::deleteCountry);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveBand(BandForm.SaveEvent event) {
        service.saveBand(event.getBand());
        updateList();
        closeEditor();
    }

    private void deleteCountry(BandForm.DeleteEvent event) {
        service.deleteBand(event.getBand());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addButtonButton = new Button("Add band");
        addButtonButton.addClickListener(click -> addBand());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addButtonButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void editBand(Band band) {
        if (band == null) {
            closeEditor();
        } else {
            form.setBand(band);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setBand(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addBand() {
        grid.asSingleSelect().clear();
        editBand(new Band());
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
        grid.setItems(service.findAllBands(filterText.getValue()));
    }
}
