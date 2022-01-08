package co.com.services.employee.service.impl;

import co.com.services.employee.domain.Employee;
import co.com.services.employee.exceptions.EmployeesInformationNotFound;
import co.com.services.employee.repository.IEmployeesRepository;
import co.com.services.employee.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final IEmployeesRepository repository;

    @Override
    public Optional<Page<Employee>> employeesInformations(Pageable paging) {
        Optional<Page<Employee>> result = this.repository.getEmployeesInformation(paging);

        if (!result.isPresent())
            throw new EmployeesInformationNotFound("There is an error getting employees information!");

        if (result.get().isEmpty())
            throw new EmployeesInformationNotFound("There is not employees information!");

        return result;
    }

    @Override
    public Optional<Employee> employeesDetailsByCode(Long code) {
        return this.repository.getEmployeesInformationByCode(code);
    }
}