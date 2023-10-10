package com.kosa.shop.entity.id;

import com.kosa.shop.entity.Item;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class ItemImgId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
    private String imgName;

}
