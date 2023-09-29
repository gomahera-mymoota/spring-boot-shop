package com.kosa.shop.jpa.mapping.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
}
