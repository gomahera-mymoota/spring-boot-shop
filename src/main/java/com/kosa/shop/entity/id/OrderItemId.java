package com.kosa.shop.entity.id;

import com.kosa.shop.entity.Item;
import com.kosa.shop.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderItemId implements Serializable {

    @ManyToOne
    private Order order;
    @ManyToOne
    private Item item;

}
