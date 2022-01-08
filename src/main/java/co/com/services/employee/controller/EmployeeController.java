package co.com.services.employee.controller;

import co.com.services.employee.domain.Employee;
import co.com.services.employee.exceptions.EmployeeCodeValueException;
import co.com.services.employee.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees/api/v1")
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService service;

    @GetMapping("/")
    public ResponseEntity<List<Employee>> all(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size) {
        Pageable paging = PageRequest.of(page, size);
       var response = this.service.employeesInformations(paging);

       return new ResponseEntity<>(response.get().getContent(), HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Employee> employeeByCode(@PathVariable String code) {
        String regex = "[0-9]+";

        if (!code.matches(regex))
            throw new EmployeeCodeValueException(String.format("Invalid value: '%s', for employee code", code));

        var response = this.service.employeesDetailsByCode(Long.parseLong(code)).get();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}