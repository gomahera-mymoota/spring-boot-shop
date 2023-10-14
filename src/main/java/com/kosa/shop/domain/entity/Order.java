package com.kosa.shop.domain.entity;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "orderItemId.order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        var order = new Order();
        order.setMember(member);

        for (var orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDERED);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    public int getTotalPrice() {    // Java Streams 버전으로 변경
//        var totalPrice = 0;
//
//        for (var orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
        var totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();

        return totalPrice;
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCELLED;

//        for (var orderItem : orderItems) {
//            orderItem.cancel();
//        }
        orderItems.stream()
                .forEach(OrderItem::cancel);
    }

}
