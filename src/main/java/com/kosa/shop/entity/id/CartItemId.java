package com.kosa.shop.entity.id;

import com.kosa.shop.entity.Cart;
import com.kosa.shop.entity.Item;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class CartItemId implements Serializable {

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Item item;
}
