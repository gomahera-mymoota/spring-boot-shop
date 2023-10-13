package com.kosa.shop.service;

import com.kosa.shop.domain.entity.Order;
import com.kosa.shop.domain.entity.OrderItem;
import com.kosa.shop.dto.OrderDto;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
import com.kosa.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email) {
        var item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        var member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        var orderItemList = new ArrayList<OrderItem>();
        var orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        var order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
