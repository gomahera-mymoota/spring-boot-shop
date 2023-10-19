package com.kosa.shop.service;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.constant.OrderStatus;
import com.kosa.shop.domain.entity.*;
import com.kosa.shop.dto.OrderDto;
import com.kosa.shop.repository.CartItemRepository;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
import com.kosa.shop.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class OrderServiceTest {

    private static final int ITEM_SIZE = 5;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    private Item prepareItem() {
        return this.prepareItem(null);
    }

    private Item prepareItem(Integer index) {
        var postfix = Optional.ofNullable(index)
                .map(String::valueOf)
                .orElse("");

        var result = new Item();
        result.setName("테스트 상품" + postfix);
        result.setPrice(10000);
        result.setDetail("테스트 상품 상세 설명" + postfix);
        result.setSellStatus(ItemSellStatus.SELL);
        result.setStockNumber(100);

        log.info("* ITEM:CREATED -> {}", result);

        return itemRepository.save(result);
    }

    private CartItem prepareCartItem(Cart cart, int count) {
        var item = prepareItem();
        var result = CartItem.createCartItem(cart, item, count);

        return cartItemRepository.save(result);
    }

    private List<Item> PrepareItems() {
        var result = new ArrayList<Item>(ITEM_SIZE);
        log.info("** ITEMS: {}", result);

        // 나는 아래 방식을 선호
        IntStream.range(0, ITEM_SIZE)
                .mapToObj(i -> prepareItem())
//                .peek(obj -> log.info("** = ITEM: {}", obj))
                .forEach(result::add);

//        IntStream.range(0, ITEM_SIZE)
//                .forEach(i -> items.add(i, prepareItem()));

        log.info("** ITEMS: {}", result);

        return result;
    }

//    private List<CartItem> prepareCartItems() {
//        var result = new ArrayList<CartItem>(ITEM_SIZE);
//        IntStream.range(0, ITEM_SIZE)
//                .mapToObj(i -> prepareCartItem())
//                .forEach(result::add);
//
//        return result;
//    }

    private <T> List<T> prepareItems(IntFunction<T> prepareItem) {
        var result = new ArrayList<T>(ITEM_SIZE);
        log.info("** ITEMS: {}", result);

        IntStream.range(0, ITEM_SIZE)
                .mapToObj(prepareItem)
                .forEach(result::add);
        log.info("** ITEMS: {}", result);

        return result;
    }

    private Member prepareMember() {
        var member = new Member();
        member.setEmail("test@email.com");

        return memberRepository.save(member);
    }

    private Cart preprareCart() {
        var member = this.prepareMember();
        var items = this.PrepareItems();
        var result = new Cart();

        return result;
    }

    @Test
    @DisplayName("주문 테스트: 상품 한 개")
    public void testOrderItem() {
        // given
        var member = prepareMember();
        var item = prepareItem();

        var orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItem(item);

        log.info("* [TEST] MEMBER:PREPARED -> {}", member);
        log.info("* [TEST] ITEM:PREPARED -> {}", item);
        log.info("* [TEST] ORDER_DTO:PREPARED -> {}", orderDto);

        var orderId = orderService.order(orderDto, member.getEmail());

        // when
        var order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // then
        var totalPrice = orderDto.getCount() * item.getPrice();

        assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
    }

    @Test
    @DisplayName("주문 테스트: 상품 여러 개")
    public void testOrderItems() {
        // given
        var member = prepareMember();
        var items = prepareItems(this::prepareItem);
        var orderDtos = items.stream()
                .map(OrderDto::new)
                .peek(dto -> dto.setCount(10))
                .toList();

        var orderId = orderService.order(orderDtos, member.getEmail());

        log.info("* [TEST] MEMBER:PREPARED -> {}", member);
        log.info("* [TEST] ITEMS:PREPARED -> {}", items);
        log.info("* [TEST] ORDER_DTOS:PREPARED -> {}", orderDtos);

        // when
        var order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // then
        var totalPrice = IntStream.range(0, ITEM_SIZE)
                        .map(i -> orderDtos.get(i).getCount() * items.get(i).getPrice())
                        .sum();
        assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
        log.info("* [TEST] TOTAL_PRICE:ACTUAL -> {}", order.getTotalPrice());
        log.info("* [TEST] TOTAL_PRICE:EXPECTED -> {}", totalPrice);
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void testCancelOrder() {
        // given
        var item = prepareItem();
        var member = prepareMember();

        var orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        var orderId = orderService.order(orderDto, member.getEmail());

        // when
        var order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(item.getStockNumber()).isEqualTo(100);
    }

    @Test
    @DisplayName("장바구니 상품 주문 테스트")
    public void testOrderCartItems() {
        // given
        // 장바구니 상품 준비
        var items = prepareItems(this::prepareItem);

        // when

        // then
    }

}