package com.kosa.shop.entity;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Item item;

    private int count;
}
