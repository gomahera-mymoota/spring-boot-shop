package com.kosa.shop.jpa.mapping.entity;

import com.kosa.shop.jpa.mapping.repository.DepartmentRepository;
import com.kosa.shop.jpa.mapping.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class EmployeeTest {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @PersistenceContext
    EntityManager em;

    public Department createDepartment(String name) {
        var dept = new Department();
        dept.setName(name);

        return dept;
    }

    @Test
    @DisplayName("findEmployeeByName 테스트: ManyToOne 테스트")
    public void findEmployeeTest() {
        var dept = this.createDepartment("Accounting");
        departmentRepository.save(dept);

        var emp = new Employee();
        emp.setName("Scott");
        emp.setSalary(2000);
        emp.setDepartment(dept);
        employeeRepository.save(emp);

        em.flush();
        em.clear();

        var savedEmp = employeeRepository.findByDepartmentId(dept.getId())
                .orElseThrow(EntityNotFoundException::new);

        Assertions.assertThat(savedEmp.get(0).getDepartment().getId()).isEqualTo(dept.getId());
    }

}