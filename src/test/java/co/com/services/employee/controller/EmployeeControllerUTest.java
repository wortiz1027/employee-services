package co.com.services.employee.controller;

import co.com.services.employee.domain.Employee;
import co.com.services.employee.exceptions.EmployeeCodeValueException;
import co.com.services.employee.service.IEmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bateria de pruebas para EmployeeController")
class EmployeeControllerUTest {

    @Mock
    IEmployeeService service;

    @InjectMocks
    EmployeeController underTest;

    @Test
    @DisplayName("Test :: Valida que se consulte la informacion de los empleados por paginacion de los resultados")
    void itShouldReturnEmployeesInformation() {
        when(this.service.employeesInformations(any())).thenReturn(employees());

        var result = this.underTest.all(0, 5);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Test :: Valida que se consulte la informacion de un empleado por 'code'")
    void itShouldReturnEmployeesInformationByCode() {
        when(this.service.employeesDetailsByCode(anyLong())).thenReturn(Optional.of(Employee.builder().build()));

        var result = this.underTest.employeeByCode("1");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Test :: Valida que retorne 'BAD_REQUEST' al consultar la informacion de un empleado por 'code'")
    void itShouldReturnEmployeesInformationByCodeBadRequest() {
        assertThatThrownBy(() -> this.underTest.employeeByCode("A"))
                .isInstanceOf(EmployeeCodeValueException.class)
                .hasMessageContaining("Invalid value: 'A', for employee code");
    }

    private Optional<Page<Employee>> employees() {
        Pageable paging = PageRequest.of(0, 5);
        return Optional.of(new PageImpl<>(List.of(), paging, 1));
    }
}