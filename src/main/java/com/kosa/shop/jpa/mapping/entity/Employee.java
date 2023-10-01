package com.kosa.shop.jpa.mapping.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer salary;

    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne
    private Department department;
}
