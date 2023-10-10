package com.kosa.shop.repository;

import com.kosa.shop.domain.entity.OrderItem;
import com.kosa.shop.domain.entity.id.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
