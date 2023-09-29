package com.kosa.shop.jpa.mapping.repository;

import com.kosa.shop.jpa.mapping.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartmentId(Long departmentId);

}
