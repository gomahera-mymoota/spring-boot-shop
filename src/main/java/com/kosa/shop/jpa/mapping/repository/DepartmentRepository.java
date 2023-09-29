package com.kosa.shop.jpa.mapping.repository;

import com.kosa.shop.jpa.mapping.entity.Department;
import com.kosa.shop.jpa.mapping.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
