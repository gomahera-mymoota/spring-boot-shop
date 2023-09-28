package com.kosa.shop.entity;

import com.kosa.shop.entity.id.ItemImgId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item_imgs")
@Getter
@Setter
public class ItemImg extends BaseEntity {

    @EmbeddedId
    private ItemImgId itemImgId;

    private String oriImgName;
    private String imgUrl;
    private Boolean isRepImg;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.itemImgId.setImgName(imgName);
        this.imgUrl = imgUrl;
    }

}
