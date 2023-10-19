package com.kosa.shop.dto;

import com.kosa.shop.domain.entity.id.CartItemId;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private CartItemId cartItemId;
    private List<CartOrderDto> cartOrderDtoList;

}
