package com.kosa.shop.repository;

import com.kosa.shop.domain.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.member.email = :email " +
            "ORDER BY o.orderDate desc"
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("SELECT count(o) " +
            "FROM Order o " +
            "WHERE o.member.email = :email"
    )
    Long countOrder(@Param("email") String email);
}
