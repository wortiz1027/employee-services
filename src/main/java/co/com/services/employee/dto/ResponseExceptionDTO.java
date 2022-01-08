package co.com.services.employee.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class ResponseExceptionDTO {

    private String code;
    private String message;
    private LocalDateTime time;

}