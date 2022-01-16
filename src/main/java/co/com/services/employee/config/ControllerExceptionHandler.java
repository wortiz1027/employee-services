package co.com.services.employee.config;

import co.com.services.employee.dto.ResponseExceptionDTO;
import co.com.services.employee.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EmployeesInformationNotFound.class)
    public ResponseEntity<ResponseExceptionDTO> employeeError(EmployeesInformationNotFound ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .code("NOT_FOUND")
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeInformationNotFound.class)
    public ResponseEntity<ResponseExceptionDTO> employeeError(EmployeeInformationNotFound ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .code("NOT_FOUND")
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeCodeValueException.class)
    public ResponseEntity<ResponseExceptionDTO> employeeError(EmployeeCodeValueException ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNameException.class)
    public ResponseEntity<ResponseExceptionDTO> fileError(FileNameException ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileSizeException.class)
    public ResponseEntity<ResponseExceptionDTO> fileError(FileSizeException ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileTypeException.class)
    public ResponseEntity<ResponseExceptionDTO> fileError(FileTypeException ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UploadFileException.class)
    public ResponseEntity<ResponseExceptionDTO> fileError(UploadFileException ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ResponseExceptionDTO> serverError(ServerException ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                                                        .code("INTERNAL_SERVER_ERROR")
                                                        .message(ex.getMessage())
                                                        .time(LocalDateTime.now())
                                                        .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}