package com.kosa.shop.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "carts")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Member member;

    public static Cart createCart(Member member) {
        var cart = new Cart();
        cart.setMember(member);

        return cart;
    }
}
