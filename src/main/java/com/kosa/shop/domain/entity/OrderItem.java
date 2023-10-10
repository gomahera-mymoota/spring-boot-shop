package com.kosa.shop.domain.entity;

import com.kosa.shop.domain.entity.id.OrderItemId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "order_items")
@Getter
@Setter
//@IdClass(OrderItemId.class)
public class OrderItem extends BaseEntity {

    @EmbeddedId
    private OrderItemId orderItemId;

//    @Id
//    @GeneratedValue
//    private Long id;

//    @Id
//    @ManyToOne
//    private Order order;
//
//    @Id
//    @ManyToOne
//    private Item item;

    private int orderPrice;
    private int count;

}
