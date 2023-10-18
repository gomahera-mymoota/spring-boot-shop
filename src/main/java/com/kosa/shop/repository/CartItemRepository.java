package com.kosa.shop.repository;

import com.kosa.shop.domain.entity.Cart;
import com.kosa.shop.domain.entity.CartItem;
import com.kosa.shop.domain.entity.id.CartItemId;
import com.kosa.shop.dto.CartDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {

    Optional<CartItem> findByCartItemId(CartItemId cartItemId);

    Optional<CartItem> findByCartItemIdCartMemberIdMemberIdAndCartItemIdItemId(Long cartId, Long itemId);

    @Query("SELECT new com.kosa.shop.dto.CartDetailDto(ci.id, i.name, i.price, ci.count, im.imgUrl) " +
            "FROM CartItem ci, ItemImg im " +
            "JOIN ci.cartItemId.item i " +
            "WHERE ci.cartItemId.cart = :cart " +
//            "WHERE ci.cartItemId.cart.id = :cartId " +
//            "WHERE ci.cart.id = :cart.id " +
            "AND im.item.id = ci.cartItemId.item.id " +
            "AND im.isRepImg = TRUE " +
            "ORDER BY ci.regTime DESC")
    List<CartDetailDto> findCartDetailDtoList(Cart cart);
//    Optional<List<CartDetailDto>> findCartDetailDtoList(Cart cart);

}
