package com.kosa.shop.jpa.mapping.repository;

import com.kosa.shop.jpa.mapping.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<List<Employee>> findByDepartmentId(Long departmentId);

}
