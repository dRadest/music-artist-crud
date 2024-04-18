package com.dradest.music.artist.crud.ui.views.artist;

import com.dradest.music.artist.crud.jpa.model.Artist;
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

@Route(value = "artists", layout = MainLayout.class)
@PageTitle("Artists")
public class ArtistView extends VerticalLayout {
    private Grid<Artist> grid = new Grid<>(Artist.class);
    private TextField filterText = new TextField();
    private ArtistForm form;
    private CrudService service;

    public ArtistView(CrudService service) {
        this.service = service;
        addClassName("artist-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ArtistForm(service.findAllCountries(null), service.findAllBands(null));
        form.setWidth("25em");
        form.addSaveListener(this::saveArtist);
        form.addDeleteListener(this::deleteArtist);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveArtist(ArtistForm.SaveEvent event) {
        service.saveArtist(event.getArtist());
        updateList();
        closeEditor();
    }

    private void deleteArtist(ArtistForm.DeleteEvent event) {
        service.deleteArtist(event.getArtist());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("artist-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName");
        grid.addColumn(artist -> artist.getCountry() != null ? artist.getCountry().getName() : "/").setHeader("Country");
        grid.addColumn(artist -> artist.getBand() != null ? artist.getBand().getName() : "/").setHeader("Band");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editArtist(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add artist");
        addContactButton.addClickListener(click -> addArtist());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editArtist(Artist artist) {
        if (artist == null) {
            closeEditor();
        } else {
            form.setArtist(artist);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setArtist(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addArtist() {
        grid.asSingleSelect().clear();
        editArtist(new Artist());
    }

    private void updateList() {
        grid.setItems(service.findAllArtists(filterText.getValue()));
    }
}
