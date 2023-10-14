package com.kosa.shop.repository;

import com.kosa.shop.domain.entity.CartItem;
import com.kosa.shop.domain.entity.id.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {

    Optional<CartItem> findByCartItemIdCartIdAndCartItemIdItemId(Long cartId, Long itemId);

//    Optional<CartItem> findByCartIdAndItemId(CartItemId cartItemId);
    Optional<CartItem> findByCartItemId(CartItemId cartItemId);

}
