package com.kosa.shop.dto;

import com.kosa.shop.domain.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {

    @NotNull(message = "상품 아이디는 반드시 입력해야 합니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개입니다.")
    private int count;

    public OrderDto(Item item) {
        this.setItem(item);
    }

    public void setItem(Item item) {
        this.itemId = item.getId();
    }

}
