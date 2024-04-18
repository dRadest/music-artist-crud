package com.dradest.music.artist.crud.ui.views.band;

import com.dradest.music.artist.crud.jpa.model.Band;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class BandForm extends FormLayout {
    private final TextField name = new TextField("Name");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button close = new Button("Cancel");

    Binder<Band> binder = new BeanValidationBinder<>(Band.class);

    public BandForm() {
        addClassName("band-form");
        add(name, createButtonsLayout());
        binder.bindInstanceFields(this);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new BandForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new BandForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new BandForm.SaveEvent(this, binder.getBean())); // <6>
        }
    }

    public void setBand(Band band) {
        // bind the values from the contact to the UI fields
        binder.setBean(band);
    }

    // Events
    public static abstract class BandFormEvent extends ComponentEvent<BandForm> {
        private Band band;

        protected BandFormEvent(BandForm source, Band band) {
            super(source, false);
            this.band = band;
        }

        public Band getBand() {
            return band;
        }
    }

    public static class SaveEvent extends BandForm.BandFormEvent {
        SaveEvent(BandForm source, Band band) {
            super(source, band);
        }
    }

    public static class DeleteEvent extends BandForm.BandFormEvent {
        DeleteEvent(BandForm source, Band band) {
            super(source, band);
        }

    }

    public static class CloseEvent extends BandForm.BandFormEvent {
        CloseEvent(BandForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<BandForm.DeleteEvent> listener) {
        return addListener(BandForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<BandForm.SaveEvent> listener) {
        return addListener(BandForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<BandForm.CloseEvent> listener) {
        return addListener(BandForm.CloseEvent.class, listener);
    }
}
