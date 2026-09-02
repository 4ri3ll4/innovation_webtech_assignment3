package auca.ac.rw.eventticket.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalDate;

@FacesValidator("notPastDateValidator")
public class NotPastDateValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        if (value == null) return;
        LocalDate date = (LocalDate) value;
        if (date.isBefore(LocalDate.now())) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Event date cannot be in the past.", null);
            throw new ValidatorException(msg);
        }
    }
}
