package com.kosa.shop.service;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.domain.entity.Item;
import com.kosa.shop.domain.entity.Member;
import com.kosa.shop.dto.CartItemDto;
import com.kosa.shop.repository.CartItemRepository;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
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
class CartServiceTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemRepository cartItemRepository;

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
    @DisplayName("장바구니 담기 테스트")
    public void addCartItem() {
        // given
        var item = saveItem();
        var member = saveMember();

        var cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        // when
        var cartItemId = cartService.addCartItem(cartItemDto, member.getEmail());
//        var cartItem = cartItemRepository.findByCartIdAndItemId(cartItemId)

        System.out.println("========================================");

        var cartItem2 = cartItemRepository.findByCartItemId(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("========================================");

        var cartItem = cartItemRepository.findByCartItemIdCartIdAndCartItemIdItemId(
                cartItemId.getCart().getId(), cartItemId.getItem().getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(cartItem.getItem().getId()).isEqualTo(item.getId());
        assertThat(cartItem.getCount()).isEqualTo(cartItemDto.getCount());
    }

}