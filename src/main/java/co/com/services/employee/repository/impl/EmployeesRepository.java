package co.com.services.employee.repository.impl;

import co.com.services.employee.domain.Employee;
import co.com.services.employee.exceptions.EmployeeInformationNotFound;
import co.com.services.employee.mapper.EmployeeMapper;
import co.com.services.employee.repository.IEmployeesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmployeesRepository implements IEmployeesRepository {

    private final JdbcTemplate template;

    @Override
    public Optional<Page<Employee>> getEmployeesInformation(Pageable paging) {
        var query = "SELECT emp.emp_no AS CODE, " +
                    "       concat(emp.first_name, ' ', emp.last_name) AS NAME, " +
                    "       CASE " +
                    "           WHEN emp.gender = 'M' THEN 'Masculine' " +
                    "           WHEN emp.gender = 'F' THEN 'Female' " +
                    "           ELSE 'Others' " +
                    "           END AS GENDER, " +
                    "       emp.hire_date AS HIRED, " +
                    "       tls.title AS TITLE, " +
                    "       CONCAT('$',FORMAT(slr.salary,2,'en_US')) AS SALARY, " +
                    "       slr.from_date AS SALARY_FROM " +
                    "FROM employees emp " +
                    "         INNER JOIN (SELECT tl.title, tl.emp_no " +
                    "                     FROM titles tl " +
                    "                              INNER JOIN (SELECT MAX(from_date) since_date, emp_no " +
                    "                                          FROM titles " +
                    "                                          GROUP BY emp_no) tmp ON tl.emp_no = tmp.emp_no " +
                    "                                                                  AND tl.from_date = tmp.since_date) tls ON emp.emp_no = tls.emp_no " +
                    "         INNER JOIN (SELECT s.salary, s.from_date, s.emp_no " +
                    "                     FROM salaries s INNER JOIN " +
                    "                          (SELECT MAX(salary) SAL, emp_no " +
                    "                           FROM salaries " +
                    "                           GROUP BY emp_no) sl ON s.emp_no = sl.emp_no " +
                    "                              AND s.salary = sl.SAL) slr ON emp.emp_no = slr.emp_no " +
                    "ORDER BY emp.emp_no ASC " +
                    "LIMIT %d OFFSET %d";

        List<Employee> result = this.template.query(String.format(query, paging.getPageSize(), paging.getOffset()), new EmployeeMapper());

        return Optional.of(new PageImpl<>(result, paging, count()));
    }

    @Override
    public Optional<Employee> getEmployeesInformationByCode(Long code) {
        var query = "SELECT emp.emp_no AS CODE,\n" +
                    "       concat(emp.first_name, ' ', emp.last_name) AS NAME,\n" +
                    "       CASE\n" +
                    "           WHEN emp.gender = 'M' THEN 'Masculine'\n" +
                    "           WHEN emp.gender = 'F' THEN 'Female'\n" +
                    "           ELSE 'Others'\n" +
                    "           END AS GENDER,\n" +
                    "       emp.hire_date AS HIRED,\n" +
                    "       tls.title AS TITLE,\n" +
                    "       CONCAT('$',FORMAT(slr.salary,2,'en_US')) AS SALARY,\n" +
                    "       slr.from_date AS SALARY_FROM\n" +
                    "FROM employees emp\n" +
                    "         INNER JOIN (SELECT tl.title, tl.emp_no\n" +
                    "                     FROM titles tl\n" +
                    "                              INNER JOIN (SELECT MAX(from_date) since_date, emp_no\n" +
                    "                                          FROM titles\n" +
                    "                                          GROUP BY emp_no) tmp ON tl.emp_no = tmp.emp_no\n" +
                    "                         AND tl.from_date = tmp.since_date) tls ON emp.emp_no = tls.emp_no\n" +
                    "         INNER JOIN (SELECT s.salary, s.from_date, s.emp_no\n" +
                    "                     FROM salaries s INNER JOIN\n" +
                    "                          (SELECT MAX(salary) SAL, emp_no\n" +
                    "                           FROM salaries\n" +
                    "                           GROUP BY emp_no) sl ON s.emp_no = sl.emp_no\n" +
                    "                              AND s.salary = sl.SAL) slr ON emp.emp_no = slr.emp_no\n" +
                    "WHERE emp.emp_no = ?";

        Object[] params = { code };
        int[] types = { Types.BIGINT };

        List<Employee> employees = this.template.query(query, params, types, new EmployeeMapper());

        if (employees.isEmpty())
            throw new EmployeeInformationNotFound(String.format("There is not information for employee with code: %s", code));

        return Optional.of(employees.get(0));
    }

    private int count() {
        return this.template.queryForObject("SELECT count(*) FROM employees", Integer.class);
    }

}