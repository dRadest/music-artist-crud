package com.dradest.music.artist.crud.ui.views.band;

import com.dradest.music.artist.crud.jpa.model.Artist;
import com.dradest.music.artist.crud.jpa.model.Band;
import com.dradest.music.artist.crud.jpa.persistence.CrudService;
import com.dradest.music.artist.crud.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

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
        grid.addColumn(
                new NativeButtonRenderer<>("Details",
                        clickedBand -> {
                            Dialog dialog = createBandDetailsDialog(clickedBand);
                            dialog.open();
                        })
        );
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editBand(event.getValue()));
    }

    private Dialog createBandDetailsDialog(Band band) {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle(band.getName());

        VerticalLayout dialogLayout = createDialogLayout(band);
        dialog.add(dialogLayout);

        Button cancelButton = new Button("Ok", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        return dialog;
    }

    private static VerticalLayout createDialogLayout(Band band) {
        TextArea membersTextArea = new TextArea("Members");
        TextArea albumsTextArea = new TextArea("Albums");

        membersTextArea.setValue(constructArtistDetails(band));
        membersTextArea.setReadOnly(true);

        albumsTextArea.setValue(constructAlbumDetails(band));
        albumsTextArea.setReadOnly(true);

        VerticalLayout dialogLayout = new VerticalLayout(membersTextArea, albumsTextArea);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private static String constructArtistDetails(Band band) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        band.getMembers().forEach(artist -> {
            String formattedArtist = String.format("%s %s (%s)",
                    artist.getFirstName(),
                    artist.getLastName(),
                    artist.getCountry() != null ? artist.getCountry().getCode() : "NA");
            stringJoiner.add(formattedArtist);
        });

        return stringJoiner.toString();
    }

    private static String constructAlbumDetails(Band band) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        band.getAlbums().forEach(album -> {
            String formattedArtist = String.format("%s (%s)",
                    album.getTitle(),
                    album.getReleaseDate() != null ? album.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy")) : "NA");
            stringJoiner.add(formattedArtist);
        });

        return stringJoiner.toString();
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
