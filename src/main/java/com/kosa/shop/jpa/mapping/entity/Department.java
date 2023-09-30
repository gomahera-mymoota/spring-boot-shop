package com.kosa.shop.jpa.mapping.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
