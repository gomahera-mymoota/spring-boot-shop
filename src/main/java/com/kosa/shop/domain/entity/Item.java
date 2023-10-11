package com.kosa.shop.domain.entity;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.dto.ItemFormDto;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "items")  // 테이블 이름을 복수형으로(책과 다름)
@Data
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(name = "price", nullable = false)
    private int price;
    @Column(nullable = false)
    private int stockNumber;
    @Lob
    @Column(nullable = false)
    private String detail;
    @Enumerated(EnumType.STRING)
    private ItemSellStatus sellStatus;

    public void updateItem(ItemFormDto itemFormDto) {
        this.name = itemFormDto.getName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.detail = itemFormDto.getDetail();
        this.sellStatus = itemFormDto.getSellStatus();
    }
}