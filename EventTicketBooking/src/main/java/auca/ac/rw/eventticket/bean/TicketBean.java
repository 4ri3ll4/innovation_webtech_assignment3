package auca.ac.rw.eventticket.bean;

import auca.ac.rw.eventticket.dao.EventDAO;
import auca.ac.rw.eventticket.dao.TicketDAO;
import auca.ac.rw.eventticket.model.Event;
import auca.ac.rw.eventticket.model.Ticket;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean(name = "ticketBean")
@ViewScoped
public class TicketBean {
    private Ticket ticket = new Ticket();
    private Long selectedEventId;
    private String message;
    private boolean error;
    private boolean editMode = false;
    private final TicketDAO ticketDAO = new TicketDAO();
    private final EventDAO eventDAO = new EventDAO();

    public String saveTicket() {
        // Business rule 1: an event must be selected
        if (selectedEventId == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please select an event for this ticket.", null));
            return null;
        }

        // Business rule 2: price and quantity must be positive
        if (ticket.getPrice() == null || ticket.getPrice() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Price must be greater than zero.", null));
            return null;
        }
        if (ticket.getQuantityAvailable() == null || ticket.getQuantityAvailable() < 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Quantity available cannot be negative.", null));
            return null;
        }

        Event event = eventDAO.getEventById(selectedEventId);
        if (event == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Selected event no longer exists.", null));
            return null;
        }
        ticket.setEvent(event);

        boolean success;
        if (editMode) {
            success = ticketDAO.updateTicket(ticket);
            message = success ? "Ticket updated successfully!" : "Update failed.";
        } else {
            success = ticketDAO.insertTicket(ticket);
            message = success ? "Ticket created successfully!" : "Ticket creation failed.";
        }
        error = !success;
        if (success) {
            resetForm();
        }
        return null;
    }

    public void editTicket(Ticket t) {
        this.ticket = ticketDAO.getTicketById(t.getId());
        this.selectedEventId = this.ticket.getEvent().getId();
        this.editMode = true;
        this.message = null;
    }

    public String deleteTicket(Long id) {
        boolean success = ticketDAO.deleteTicket(id);
        message = success ? "Ticket deleted successfully!" : "Delete failed.";
        error = !success;
        if (editMode && ticket.getId() != null && ticket.getId().equals(id)) {
            resetForm();
        }
        return null;
    }

    public void cancelEdit() {
        resetForm();
    }

    private void resetForm() {
        ticket = new Ticket();
        selectedEventId = null;
        editMode = false;
    }

    public List<Ticket> getTickets() {
        return ticketDAO.getAllTickets();
    }

    public List<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    // Getters & setters
    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public Long getSelectedEventId() { return selectedEventId; }
    public void setSelectedEventId(Long selectedEventId) { this.selectedEventId = selectedEventId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
