package com.dradest.music.artist.crud.ui.views.album;

import com.dradest.music.artist.crud.jpa.model.AlbumSong;
import com.dradest.music.artist.crud.jpa.model.Song;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class AlbumTracklistField extends CustomField<Set<AlbumSong>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumTracklistField.class);

    private TextField title = new TextField("Title");
    private IntegerField length = new IntegerField("Length");
    private Button addButton = new Button("Add song");

    private Grid<Song> grid = new Grid<>(Song.class);
    private Set<Song> songSet = new HashSet<>();
    private int position = 0;
    private Set<AlbumSong> value = new HashSet<>();

    Binder<Song> binder = new BeanValidationBinder<>(Song.class);

    public AlbumTracklistField() {
        configureGrid();
        configureBinder();

        addButton.addClickListener(event -> validateAndSave());

        HorizontalLayout horizontalLayout = new HorizontalLayout(title, length, addButton);
//        HorizontalLayout horizontalLayout = new HorizontalLayout(grid);
        VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, grid);
        verticalLayout.setSizeFull();

        add(verticalLayout);
    }

    private void configureGrid() {
        grid.addClassNames("song-grid");
        grid.setSizeFull();
        grid.setColumns("title", "length");

        grid.setWidthFull();
        //grid.setHeightFull();
        grid.setMinHeight("300px");
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureBinder() {
        // Create bindings between UI fields and the SnackOrder data model
        binder.forField(title)
                .asRequired("Name is required")
                .bind("title");
        binder.forField(length)
                .asRequired("Length is required")
                .bind("length");

        // Only enable add button when the form is valid.
        binder.addStatusChangeListener(status -> {
            addButton.setEnabled(binder.isValid());
        });
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            Song aSong = new Song();
            try {
                binder.writeBean(aSong);
            } catch (ValidationException e) {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
            songSet.add(aSong);
            grid.getDataProvider().refreshAll();
            binder.setBean(new Song());

            AlbumSong albumSong = new AlbumSong();
            albumSong.setPosition(++position);
            albumSong.setSong(aSong);
            value.add(albumSong);
        } else {
            LOGGER.warn("Song binder validation FAILED");
        }
    }

    @Override
    protected Set<AlbumSong> generateModelValue() {
/*        Set<AlbumSong> albumSongs = new HashSet<>();
        int position = 1;
        for (Song aSong : songSet) {
            AlbumSong albumSong = new AlbumSong();
            albumSong.setPosition(position);
            albumSong.setSong(aSong);
            albumSongs.add(albumSong);
            position++;
        }*/
        return value;
    }

    @Override
    protected void setPresentationValue(Set<AlbumSong> albumSongs) {
        songSet.clear();
        value.clear();
        position = 0;
        if (albumSongs == null) {
            LOGGER.warn("NULL albumSongs passed to setPresentationValue");
        } else {
            for (AlbumSong albumSong : albumSongs) {
                songSet.add(albumSong.getSong());
            }
            value = albumSongs;
        }

        // set data provider on grid
        ListDataProvider<Song> dataProvider =
                DataProvider.ofCollection(songSet);
        grid.setDataProvider(dataProvider);
        dataProvider.refreshAll();
    }
}
