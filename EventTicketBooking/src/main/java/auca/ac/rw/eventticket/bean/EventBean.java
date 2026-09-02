package auca.ac.rw.eventticket.bean;

import auca.ac.rw.eventticket.dao.EventDAO;
import auca.ac.rw.eventticket.model.Event;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.time.LocalDate;
import java.util.List;

@ManagedBean(name = "eventBean")
@ViewScoped
public class EventBean {
    private Event event = new Event();
    private String message;
    private boolean error;
    private boolean editMode = false;
    private final EventDAO eventDAO = new EventDAO();

    public String saveEvent() {
        // Business rule 1: no duplicate event titles
        Long excludeId = editMode ? event.getId() : null;
        if (eventDAO.existsByTitle(event.getTitle(), excludeId)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An event with this title already exists.", null));
            return null;
        }

        // Business rule 2: sanity bound on the event date
        if (event.getEventDate().getYear() > LocalDate.now().getYear() + 10) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Event date is too far in the future to be realistic.", null));
            return null;
        }

        boolean success;
        if (editMode) {
            success = eventDAO.updateEvent(event);
            message = success ? "Event updated successfully!" : "Update failed.";
        } else {
            success = eventDAO.insertEvent(event);
            message = success ? "Event created successfully!" : "Event creation failed.";
        }
        error = !success;
        if (success) {
            resetForm();
        }
        return null;
    }

    public void editEvent(Event e) {
        this.event = eventDAO.getEventById(e.getId());
        this.editMode = true;
        this.message = null;
    }

    public String deleteEvent(Long id) {
        boolean success = eventDAO.deleteEvent(id);
        message = success ? "Event deleted successfully!" : "Delete failed (it may still have tickets attached).";
        error = !success;
        if (editMode && event.getId() != null && event.getId().equals(id)) {
            resetForm();
        }
        return null;
    }

    public void cancelEdit() {
        resetForm();
    }

    private void resetForm() {
        event = new Event();
        editMode = false;
    }

    public List<Event> getEvents() {
        return eventDAO.getAllEvents();
    }

    // Getters & setters
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
