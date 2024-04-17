package com.dradest.music.artist.crud.ui.views;

import com.dradest.music.artist.crud.jpa.model.Country;
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

public class CountryForm extends FormLayout {
    private final TextField name = new TextField("Name");
    private final TextField code = new TextField("Code");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button close = new Button("Cancel");

    Binder<Country> binder = new BeanValidationBinder<>(Country.class);

    public CountryForm() {
        addClassName("country-form");
        add(name, code, createButtonsLayout());
        binder.bindInstanceFields(this);
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
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }

    public void setCountry(Country country) {
        // bind the values from the contact to the UI fields
        binder.setBean(country);
    }

    // Events
    public static abstract class CountryFormEvent extends ComponentEvent<CountryForm> {
        private Country country;

        protected CountryFormEvent(CountryForm source, Country country) {
            super(source, false);
            this.country = country;
        }

        public Country getCountry() {
            return country;
        }
    }

    public static class SaveEvent extends CountryFormEvent {
        SaveEvent(CountryForm source, Country country) {
            super(source, country);
        }
    }

    public static class DeleteEvent extends CountryFormEvent {
        DeleteEvent(CountryForm source, Country country) {
            super(source, country);
        }

    }

    public static class CloseEvent extends CountryFormEvent {
        CloseEvent(CountryForm source) {
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
