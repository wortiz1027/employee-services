package co.com.services.employee.service;

import co.com.services.employee.domain.Employee;
import co.com.services.employee.exceptions.EmployeesInformationNotFound;
import co.com.services.employee.repository.IEmployeesRepository;
import co.com.services.employee.service.impl.EmployeeService;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bateria de pruebas para EmployeeService")
class EmployeeServiceUTest {

    @Mock
    IEmployeesRepository repository;

    @InjectMocks
    EmployeeService underTest;

    @Test
    @DisplayName("Test :: Valida que se consulte la informacion de los empleados por paginacion de los resultados")
    void itShouldReturnEmployeesInformation() {
        when(this.repository.getEmployeesInformation(any())).thenReturn(employees());

        var result = this.underTest.employeesInformations(pageable());

        assertThat(Boolean.TRUE).isEqualTo(result.isPresent());
    }

    @Test
    @DisplayName("Test :: Valida que retorne 'NOT_FOUND' al consultar la informacion de los empleados")
    void itShouldReturnEmployeesInformationNotFound0() {
        when(this.repository.getEmployeesInformation(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.underTest.employeesInformations(pageable()))
                                                .isInstanceOf(EmployeesInformationNotFound.class)
                                                .hasMessageContaining("There is an error getting employees information!");
    }

    @Test
    @DisplayName("Test :: Valida que retorne 'NOT_FOUND' al consultar la informacion de los empleados")
    void itShouldReturnEmployeesInformationNotFound1() {
        when(this.repository.getEmployeesInformation(any())).thenReturn(employeesEmpty());
        assertThatThrownBy(() -> this.underTest.employeesInformations(pageable()))
                                                .isInstanceOf(EmployeesInformationNotFound.class)
                                                .hasMessageContaining("There is not employees information!");
    }

    @Test
    @DisplayName("Test :: Valida que consulte informacion del empleado por 'code'")
    void itShouldReturnEmployeesInformationByCode() {
        when(this.repository.getEmployeesInformationByCode(anyLong())).thenReturn(Optional.of(Employee.builder().build()));

        var result = this.underTest.employeesDetailsByCode(1L);

        assertThat(Boolean.TRUE).isEqualTo(result.isPresent());
    }

    private Optional<Page<Employee>> employees() {
        Pageable paging = PageRequest.of(0, 5);

        return Optional.of(new PageImpl<>(List.of(Employee.builder().build()), paging, 1));
    }

    private Optional<Page<Employee>> employeesEmpty() {
        Pageable paging = PageRequest.of(0, 5);

        return Optional.of(new PageImpl<>(List.of(), paging, 1));
    }

    private Pageable pageable() {
        return PageRequest.of(0, 5);
    }

}