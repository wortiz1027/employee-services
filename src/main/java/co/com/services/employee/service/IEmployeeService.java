package co.com.services.employee.service;

import co.com.services.employee.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IEmployeeService {

    Optional<Page<Employee>> employeesInformations(Pageable paging);
    Optional<Employee> employeesDetailsByCode(Long code);

}