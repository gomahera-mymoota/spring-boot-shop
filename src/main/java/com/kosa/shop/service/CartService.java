package com.kosa.shop.service;

import com.kosa.shop.domain.entity.Cart;
import com.kosa.shop.domain.entity.CartItem;
import com.kosa.shop.domain.entity.id.CartItemId;
import com.kosa.shop.dto.CartItemDto;
import com.kosa.shop.repository.CartItemRepository;
import com.kosa.shop.repository.CartRepository;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    // method 이름 변경 <- addCart()
    // 반환 타입 변경 <- Long
    public CartItemId addCartItem(CartItemDto cartItemDto, String email) {
        var item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        var member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        var cart = cartRepository.findByMemberId(member.getId())
                .orElseGet(() -> cartRepository.save(Cart.createCart(member)));

//        var cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId())
        var cartItem = cartItemRepository.findByCartItemIdCartIdAndCartItemIdItemId(cart.getId(), item.getId())
                .orElseGet(() -> cartItemRepository.save(
                        CartItem.createCartItem(cart, item, 0)
                ));

        cartItem.increaseCount(cartItemDto.getCount());

        return cartItem.getCartItemId();
    }
}
