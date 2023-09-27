package com.kosa.shop.entity;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.entity.id.OrderItemId;
import com.kosa.shop.repository.ItemRepository;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

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

    @Test
    @DisplayName(" 영속성 전이 테스트")
    public void cascadeTest() {
        // given
        var order = new Order();
        for (int i = 0; i < 3; i++) {
            var item = this.createItem();
            itemRepository.save(item);

            var orderItemId = new OrderItemId();
            orderItemId.setItem(item);
            orderItemId.setOrder(order);

            var orderItem = new OrderItem();
            orderItem.setOrderItemId(orderItemId);
//            orderItem.setItem(item);
//            orderItem.setOrder(order);
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
}