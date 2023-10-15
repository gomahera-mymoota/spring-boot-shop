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

    private int count;
//    @Id
//    @ManyToOne
//    private Cart cart;

//    @Id
//    @ManyToOne
//    private Cart cart;
//
//    @ManyToOne
//    private Item item;

    public CartItem() {
        this.cartItemId = new CartItemId();
    }

    public Cart getCart() {
        return this.cartItemId.getCart();
    }

    public void setCart(Cart cart) {
        this.cartItemId.setCart(cart);
    }

    public Item getItem() {
        return this.cartItemId.getItem();
    }

    public void setItem(Item item) {
        this.cartItemId.setItem(item);
    }

    public static CartItem createCartItem(Cart cart, Item item, int count) {
        var cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;
    }

    public void increaseCount(int count) {
        this.count += count;
    }

    public void updateCount(int count) {
        this.count = count;
    }

}
