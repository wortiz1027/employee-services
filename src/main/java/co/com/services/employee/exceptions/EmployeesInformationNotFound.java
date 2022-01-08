package co.com.services.employee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeesInformationNotFound extends RuntimeException {

    static final long serialVersionUID = -1L;

    public EmployeesInformationNotFound() {
        super();
    }

    public EmployeesInformationNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeesInformationNotFound(String message) {
        super(message);
    }

    public EmployeesInformationNotFound(Throwable cause) {
        super(cause);
    }

}