package com.kosa.shop.service;

import com.kosa.shop.domain.entity.Order;
import com.kosa.shop.domain.entity.OrderItem;
import com.kosa.shop.dto.OrderDto;
import com.kosa.shop.dto.OrderHistDto;
import com.kosa.shop.dto.OrderItemDto;
import com.kosa.shop.repository.ItemImgRepository;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
import com.kosa.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        var member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        var item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        var orderItemList = new ArrayList<OrderItem>();
        var orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        var order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    public Long order(List<OrderDto> orderDtoList, String email) {
        var member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        var itemList = orderDtoList.stream()
                .mapToLong(OrderDto::getItemId)
                .mapToObj(itemId -> itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new))
                .toList();

        var orderItemList = IntStream.range(0, itemList.size())
                .mapToObj(i -> OrderItem.createOrderItem(itemList.get(i), orderDtoList.get(i).getCount()))
                .toList();

        var order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        var orders = orderRepository.findOrders(email, pageable);
        var totalCount = orderRepository.countOrder(email);
        var orderHistDtos = new ArrayList<OrderHistDto>();

        for (var order : orders) {
            var orderHistDto = new OrderHistDto(order);
            var orderItems = order.getOrderItems();

            for (var orderItem : orderItems) {
                var itemImg = itemImgRepository.findByItemIdAndIsRepImgTrue(orderItem.getItem().getId());
                var orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        var member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        var order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        var savedMember = order.getMember();

        if (!StringUtils.equals(member.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        order.cancel();
    }

}
