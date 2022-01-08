package co.com.services.employee.repository;

import co.com.services.employee.domain.Employee;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = {"employees"})
public interface IEmployeesRepository {

    @Cacheable
    Optional<Page<Employee>> getEmployeesInformation(Pageable paging);

    @Cacheable
    Optional<Employee> getEmployeesInformationByCode(Long code);

}