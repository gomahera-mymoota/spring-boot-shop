package com.kosa.shop.domain.entity;

import com.kosa.shop.domain.entity.id.OrderItemId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    public OrderItem() {
        this.orderItemId = new OrderItemId();
    }
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

    public Order getOrder() {
        return this.orderItemId.getOrder();
    }
    public void setOrder(Order order) {
        this.orderItemId.setOrder(order);
    }

    public Item getItem() {
        return this.orderItemId.getItem();
    }
    public void setItem(Item item) {
        this.orderItemId.setItem(item);
    }

    public static OrderItem createOrderItem(Item item, int count) {
        var orderItem = new OrderItem();
//        orderItem.getOrderItemId().setItem(item);
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        item.decreaseStock(count);

        return orderItem;
    }

    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }

    public void cancel() {
        this.getItem().increaseStock(count);
    }

}
