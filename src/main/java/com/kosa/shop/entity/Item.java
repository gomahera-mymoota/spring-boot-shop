package com.kosa.shop.entity;

import com.kosa.shop.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Item {

    @Id
    @Column(name = "item_id")
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

    private LocalDateTime regTime;
    private LocalDateTime updateTime;

}