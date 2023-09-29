package com.kosa.shop.jpa.mapping.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    private Integer salary;

    @ManyToOne
    private Department department;
}
