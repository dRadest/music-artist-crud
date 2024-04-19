package com.dradest.music.artist.crud.ui.views.album;

import com.dradest.music.artist.crud.jpa.model.Album;
import com.dradest.music.artist.crud.jpa.model.Band;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class AlbumForm extends FormLayout {
    TextField title = new TextField("Title");
    TextField recordLabel = new TextField("Record label");
    DatePicker releaseDate = new DatePicker("Release date");
    ComboBox<Band> band = new ComboBox<>("Band");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Album> binder = new BeanValidationBinder<>(Album.class);

    public AlbumForm(List<Band> bands) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        band.setItems(bands);
        band.setItemLabelGenerator(Band::getName);

        add(title,
                recordLabel,
                releaseDate,
                band,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setAlbum(Album album) {
        binder.setBean(album);
    }

    public static abstract class AlbumFormEvent extends ComponentEvent<AlbumForm> {
        private Album album;

        protected AlbumFormEvent(AlbumForm source, Album album) {


            super(source, false);
            this.album = album;
        }

        public Album getAlbum() {
            return album;
        }
    }

    public static class SaveEvent extends AlbumFormEvent {
        SaveEvent(AlbumForm source, Album album) {
            super(source, album);
        }
    }

    public static class DeleteEvent extends AlbumFormEvent {
        DeleteEvent(AlbumForm source, Album album) {
            super(source, album);
        }

    }

    public static class CloseEvent extends AlbumFormEvent {
        CloseEvent(AlbumForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {


        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
