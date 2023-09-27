package com.kosa.shop.entity;

import com.kosa.shop.entity.id.OrderItemId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Getter
@Setter
//@IdClass(OrderItemId.class)
public class OrderItem implements Serializable {

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

    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
