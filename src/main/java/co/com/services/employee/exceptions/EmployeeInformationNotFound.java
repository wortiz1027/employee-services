package co.com.services.employee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeeInformationNotFound extends RuntimeException {

    static final long serialVersionUID = -1L;

    public EmployeeInformationNotFound() {
        super();
    }

    public EmployeeInformationNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeInformationNotFound(String message) {
        super(message);
    }

    public EmployeeInformationNotFound(Throwable cause) {
        super(cause);
    }

}