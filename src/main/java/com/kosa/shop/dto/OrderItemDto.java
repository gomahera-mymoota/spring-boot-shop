package com.kosa.shop.dto;

import com.kosa.shop.domain.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String name;
    private int count;
    private int orderPrice;
    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.name = orderItem.getItem().getName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
