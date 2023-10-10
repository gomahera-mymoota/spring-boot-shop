package com.kosa.shop.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item_imgs", indexes = @Index(columnList = "item_id"))
@Getter
@Setter
public class ItemImg extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Item item;
    private String oriImgName;
    private String imgName;
    private String imgUrl;
    private Boolean isRepImg;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
