package co.com.services.employee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileNameException extends RuntimeException {

    static final long serialVersionUID = -1L;

    public FileNameException() {
        super();
    }

    public FileNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNameException(String message) {
        super(message);
    }

    public FileNameException(Throwable cause) {
        super(cause);
    }

}