package co.com.services.employee.repository;

import co.com.services.employee.domain.Employee;
import co.com.services.employee.exceptions.EmployeeInformationNotFound;
import co.com.services.employee.exceptions.EmployeesInformationNotFound;
import co.com.services.employee.mapper.EmployeeMapper;
import co.com.services.employee.repository.impl.EmployeesRepository;
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
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bateria de pruebas para EmployeesRepository")
class EmployeesRepositoryUTest {

    @Mock
    JdbcTemplate template;

    @InjectMocks
    EmployeesRepository underTest;

    @Test
    @DisplayName("Test :: Valida que se consulte la informacion de los empleados por paginacion de los resultados")
    void itShouldReturnEmployeesInformation() {
        when(this.template.query(anyString(), any(EmployeeMapper.class))).thenReturn(List.of(Employee.builder().build()));
        when(this.template.queryForObject(anyString(), eq(Integer.class))).thenReturn(count());

        var result = this.underTest.getEmployeesInformation(pageable());

        assertThat(Boolean.TRUE).isEqualTo(result.isPresent());
    }

    @Test
    @DisplayName("Test :: Valida que se consulte la informacion del empleado por 'code'")
    void itShouldReturnEmployeesInformationByCode() {
        when(this.template.query(anyString(), any(Object[].class), any(int[].class), any(EmployeeMapper.class))).thenReturn(List.of(Employee.builder().build()));

        var result = this.underTest.getEmployeesInformationByCode(1L);

        assertThat(Boolean.TRUE).isEqualTo(result.isPresent());
    }

    @Test
    @DisplayName("Test :: Valida que al consultar el empleado este no existe")
    void itShouldReturnEmployeesInformationByCodeEmpty() {
        when(this.template.query(anyString(), any(Object[].class), any(int[].class), any(EmployeeMapper.class))).thenReturn(List.of());

        assertThatThrownBy(() -> this.underTest.getEmployeesInformationByCode(1L))
                .isInstanceOf(EmployeeInformationNotFound.class)
                .hasMessageContaining("There is not information for employee with code: 1");
    }

    private Optional<Page<Employee>> employees() {
        Pageable paging = PageRequest.of(0, 5);

        return Optional.of(new PageImpl<>(List.of(Employee.builder().build()), paging, 1));
    }

    private Pageable pageable() {
        return PageRequest.of(0, 5);
    }

    private Integer count() {
        return 1;
    }

}