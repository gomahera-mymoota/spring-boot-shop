package com.kosa.shop.controller;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.domain.entity.Item;
import com.kosa.shop.domain.entity.Member;
import com.kosa.shop.dto.OrderDto;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
import com.kosa.shop.repository.OrderRepository;
import com.kosa.shop.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderControllerTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;

    public Item saveItem() {
        var item = new Item();
        item.setName("테스트 상품");
        item.setPrice(10000);
        item.setDetail("테스트 상품 상세 설명");
        item.setSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return itemRepository.save(item);
    }

    public Member saveMember() {
        var member = new Member();
        member.setEmail("test@email.com");

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order() {
        // given
        var item = saveItem();
        var member = saveMember();

        var orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        var orderId = orderService.order(orderDto, member.getEmail());

        // when
        var order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        var orderItems = order.getOrderItems();
        var totalPrice = orderDto.getCount() * item.getPrice();

        // then
        assertThat(totalPrice).isEqualTo(order.getTotalPrice());
    }

}