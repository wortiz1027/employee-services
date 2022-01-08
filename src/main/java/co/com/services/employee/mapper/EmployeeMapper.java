package co.com.services.employee.mapper;

import co.com.services.employee.domain.Employee;
import co.com.services.employee.exceptions.ServerException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Employee employee = Employee.builder()
                                        .code(rs.getLong("CODE"))
                                        .name(rs.getString("NAME"))
                                        .gender(rs.getString("GENDER"))
                                        .hired(LocalDate.parse(rs.getString("HIRED"), formatter))
                                        .title(rs.getString("TITLE"))
                                        .salary(rs.getString("SALARY"))
                                        .salaryFrom(LocalDate.parse(rs.getString("SALARY_FROM"), formatter))
                                        .build();

            return employee;
        } catch (RuntimeException re) {
            throw new ServerException(String.format("There is an error processing employees information: %s", re.getMessage()));
        }
    }
}