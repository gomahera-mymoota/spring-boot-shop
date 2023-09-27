package com.kosa.shop.entity;

import com.kosa.shop.entity.id.CartItemId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "carts")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Member member;
}
