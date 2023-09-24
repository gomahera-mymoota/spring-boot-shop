package com.kosa.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {

    private Long id;

    private String name;

    private Integer price;

    private String detail;

    private String sellStatCond;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
