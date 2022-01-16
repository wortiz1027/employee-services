package co.com.services.employee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileSizeException extends RuntimeException {

    static final long serialVersionUID = -1L;

    public FileSizeException() {
        super();
    }

    public FileSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSizeException(String message) {
        super(message);
    }

    public FileSizeException(Throwable cause) {
        super(cause);
    }

}