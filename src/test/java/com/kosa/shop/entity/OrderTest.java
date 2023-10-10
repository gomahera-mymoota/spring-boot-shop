package com.kosa.shop.entity;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.domain.entity.Item;
import com.kosa.shop.domain.entity.Member;
import com.kosa.shop.domain.entity.Order;
import com.kosa.shop.domain.entity.OrderItem;
import com.kosa.shop.domain.entity.id.OrderItemId;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
import com.kosa.shop.repository.OrderItemRepository;
import com.kosa.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        var item = new Item();
        item.setName("테스트 상품");
        item.setPrice(10000);
        item.setDetail("상세 설명");
        item.setSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    public Order createOrder() {
        var order = new Order();
        for (int i = 0; i < 3; i++) {
            var item = this.createItem();
            itemRepository.save(item);

            var orderItem = new OrderItem();
            orderItem.setOrderItemId(new OrderItemId(order, item));
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);

            order.getOrderItems().add(orderItem);
        }

        var member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        // given
        var order = new Order();
        for (int i = 0; i < 3; i++) {
            var item = this.createItem();
            itemRepository.save(item);

            var orderItemId = new OrderItemId(order, item);
//            orderItemId.setItem(item);
//            orderItemId.setOrder(order);

            var orderItem = new OrderItem();
            orderItem.setOrderItemId(orderItemId);
            orderItem.setCount(10);
            orderItem.setOrderPrice(10000);

            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        // when
        var savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest() {
        var order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        var order = this.createOrder();
        var orderItemId = order.getOrderItems().get(0).getOrderItemId();
        em.flush();
        em.clear();

        var orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class: " + orderItem.getOrderItemId().getOrder().getClass());

        System.out.println("==================================");
        System.out.println(orderItem.getOrderItemId().getOrder().getOrderDate());
        System.out.println("==================================");
    }
}