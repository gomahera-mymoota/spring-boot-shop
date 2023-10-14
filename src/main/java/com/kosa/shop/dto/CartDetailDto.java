package com.kosa.shop.dto;

import com.kosa.shop.domain.entity.id.CartItemId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {

    private CartItemId cartItemId;
    private String name;
    private int price;
    private int count;
    private String imgUrl;

    public CartDetailDto(CartItemId cartItemId, String name, int price,
                         int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.name = name;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }

}
