package com.kosa.shop.service;

import com.kosa.shop.domain.entity.Cart;
import com.kosa.shop.domain.entity.CartItem;
import com.kosa.shop.domain.entity.Member;
import com.kosa.shop.domain.entity.id.CartItemId;
import com.kosa.shop.dto.CartDetailDto;
import com.kosa.shop.dto.CartItemDto;
import com.kosa.shop.repository.CartItemRepository;
import com.kosa.shop.repository.CartRepository;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

        var cartItem = cartItemRepository.findByCartItemId(new CartItemId(cart, item))
                .orElseGet(() -> cartItemRepository.save(
                        CartItem.createCartItem(cart, item, 0)
                ));

        cartItem.increaseCount(cartItemDto.getCount());

        return cartItem.getCartItemId();
    }

    // Optional을 적극적으로 사용한 것에 주의
    // 메소드 이름 변경 <- getCartList
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartItemList(String email) {
//        var cartDetailDtoList = new ArrayList<CartDetailDto>();
        var member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        var cartDetailDtoList = cartRepository.findByMemberId(member.getId())
                .map(cart -> cartItemRepository.findCartDetailDtoList(cart.getId()))
                .orElseGet(ArrayList<CartDetailDto>::new);

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        var member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        var item = itemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        var cartItem = cartItemRepository.findByCartItemId(new CartItemId(member.getCart(), item))
                .orElseThrow(EntityNotFoundException::new);

        var savedMember = cartItem.getCart().getMember();

        return StringUtils.equals(member.getEmail(), savedMember.getEmail());
    }

    public void updateCartItemCount(CartItemId cartItemId, int count) {
        var cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void updateCartItemCount(Long cartItemId, String email, int count) {
        var member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        var item = itemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        this.updateCartItemCount(new CartItemId(member.getCart(), item), count);
    }

}
