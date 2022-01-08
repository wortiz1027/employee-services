package co.com.services.employee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmployeeCodeValueException extends RuntimeException {

    static final long serialVersionUID = -1L;

    public EmployeeCodeValueException() {
        super();
    }

    public EmployeeCodeValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeCodeValueException(String message) {
        super(message);
    }

    public EmployeeCodeValueException(Throwable cause) {
        super(cause);
    }

}