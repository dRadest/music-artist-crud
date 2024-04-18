package com.dradest.music.artist.crud.ui.views.artist;

import com.dradest.music.artist.crud.jpa.model.Artist;
import com.dradest.music.artist.crud.jpa.model.Band;
import com.dradest.music.artist.crud.jpa.model.Country;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ArtistForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    ComboBox<Country> country = new ComboBox<>("Country");
    ComboBox<Band> band = new ComboBox<>("Band");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    // Other fields omitted
    Binder<Artist> binder = new BeanValidationBinder<>(Artist.class);

    public ArtistForm(List<Country> countries, List<Band> bands) {
        addClassName("artist-form");
        binder.bindInstanceFields(this);

        country.setItems(countries);
        country.setItemLabelGenerator(Country::getName);
        band.setItems(bands);
        band.setItemLabelGenerator(Band::getName);

        add(firstName,
                lastName,
                country,
                band,
                createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // <2>
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }


    public void setArtist(Artist artist) {
        binder.setBean(artist); // <1>
    }

    // Events
    public static abstract class ArtistFormEvent extends ComponentEvent<ArtistForm> {
        private Artist artist;

        protected ArtistFormEvent(ArtistForm source, Artist artist) {
            super(source, false);
            this.artist = artist;
        }

        public Artist getArtist() {
            return artist;
        }
    }

    public static class SaveEvent extends ArtistFormEvent {
        SaveEvent(ArtistForm source, Artist artist) {
            super(source, artist);
        }
    }

    public static class DeleteEvent extends ArtistFormEvent {
        DeleteEvent(ArtistForm source, Artist artist) {
            super(source, artist);
        }

    }

    public static class CloseEvent extends ArtistFormEvent {
        CloseEvent(ArtistForm source) {
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
