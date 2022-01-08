package co.com.services.employee.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
public class Employee {

    private Long code;
    private String name;
    private String gender;
    private LocalDate hired;
    private String title;
    private String salary;
    private LocalDate salaryFrom;

}