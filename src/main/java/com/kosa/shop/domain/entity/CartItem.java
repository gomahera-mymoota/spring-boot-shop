package com.kosa.shop.domain.entity;

import com.kosa.shop.domain.entity.id.CartItemId;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem extends BaseEntity {

    @EmbeddedId
    private CartItemId cartItemId;

//    @Id
//    @ManyToOne
//    private Cart cart;

//    @Id
//    @ManyToOne
//    private Cart cart;
//
//    @ManyToOne
//    private Item item;

    private int count;
}
