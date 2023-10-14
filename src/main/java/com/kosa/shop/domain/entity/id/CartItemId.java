package com.kosa.shop.domain.entity.id;

import com.kosa.shop.domain.entity.Cart;
import com.kosa.shop.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CartItemId implements Serializable {

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Item item;
}
