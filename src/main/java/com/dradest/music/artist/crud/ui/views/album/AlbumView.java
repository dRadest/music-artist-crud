package com.dradest.music.artist.crud.ui.views.album;

import com.dradest.music.artist.crud.jpa.model.Album;
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

@Route(value = "albums", layout = MainLayout.class)
@PageTitle("Albums")
public class AlbumView extends VerticalLayout {
    private Grid<Album> grid = new Grid<>(Album.class);
    private TextField filterText = new TextField();
    private AlbumForm form;
    private CrudService service;

    public AlbumView(CrudService service) {
        this.service = service;
        addClassName("album-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new AlbumForm(service.findAllBands(null));
        form.setWidth("60em");
        form.addSaveListener(this::saveAlbum);
        form.addDeleteListener(this::deleteAlbum);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveAlbum(AlbumForm.SaveEvent event) {
        service.saveAlbum(event.getAlbum());
        updateList();
        closeEditor();
    }

    private void deleteAlbum(AlbumForm.DeleteEvent event) {
        service.deleteAlbum(event.getAlbum());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("album-grid");
        grid.setSizeFull();
        grid.setColumns("title", "recordLabel", "releaseDate");
        grid.addColumn(album -> album.getBand() != null ? album.getBand().getName() : "/").setHeader("Band");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editAlbum(event.getValue()));
    }

    public void editAlbum(Album album) {
        if (album == null) {
            closeEditor();
        } else {
            form.setAlbum(album);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setAlbum(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addAlbum() {
        grid.asSingleSelect().clear();
        editAlbum(new Album());
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by title...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addAlbumButton = new Button("Add album");
        addAlbumButton.addClickListener(click -> addAlbum());

        var toolbar = new HorizontalLayout(filterText, addAlbumButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(service.findAllAlbums(filterText.getValue()));
    }
}
