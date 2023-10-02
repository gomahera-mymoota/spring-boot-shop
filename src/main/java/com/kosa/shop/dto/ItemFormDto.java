package com.kosa.shop.dto;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 반드시 입력해야 합니다.")
    private String name;                    // 교재에서 이름: itemNm
    @NotNull(message = "가격은 반드시 입력해야 합니다.")
    private Integer price;
    @NotBlank(message = "상품 상세 정보는 반드시 입력해야 합니다.")
    private String detail;                  // 교재에서 이름: itemDetail
    @NotNull(message = "상품 재고는 반드시 입력해야 합니다.")
    private Integer stockNumber;

    private ItemSellStatus sellStatus;      // 교재에서 이름: itemSellStatus
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
