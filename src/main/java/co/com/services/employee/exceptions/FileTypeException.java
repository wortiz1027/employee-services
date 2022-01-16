package co.com.services.employee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileTypeException extends RuntimeException {

    static final long serialVersionUID = -1L;

    public FileTypeException() {
        super();
    }

    public FileTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileTypeException(String message) {
        super(message);
    }

    public FileTypeException(Throwable cause) {
        super(cause);
    }

}