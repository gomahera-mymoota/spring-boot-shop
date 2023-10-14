package com.kosa.shop.service;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.domain.entity.Item;
import com.kosa.shop.domain.entity.ItemImg;
import com.kosa.shop.domain.entity.Member;
import com.kosa.shop.dto.CartItemDto;
import com.kosa.shop.functional.ExceptionFunction;
import com.kosa.shop.repository.CartItemRepository;
import com.kosa.shop.repository.ItemRepository;
import com.kosa.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    private ItemImgService itemImgService;

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

    MultipartFile getMultipartFile() throws Exception {
        var path = "C:/Devs/shop/item/test/";
        var imageName = "image0.jpg";
        var multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});

        return multipartFile;
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

        System.out.println("========================================");

        // 아래 버전이 더 효율적
        var cartItem = cartItemRepository.findByCartItemId(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("========================================");

        var cartItem2 = cartItemRepository.findByCartItemIdCartIdAndCartItemIdItemId(
                cartItemId.getCart().getId(), cartItemId.getItem().getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(cartItem.getItem().getId()).isEqualTo(item.getId());
        assertThat(cartItem.getCount()).isEqualTo(cartItemDto.getCount());
    }

    @Test
    @DisplayName("장바구니 목록 가져오기 테스트")
    public void getCartItemList() throws Exception {
        // given
        var member = saveMember();
        var items = List.of(new Item[]{ saveItem(), saveItem(), saveItem() });

        items.stream()
                .map(ItemImg::new)
                .forEach(itemImg -> {
                    try {
                        itemImg.setIsRepImg(true);
                        itemImgService.saveItemImg(itemImg, getMultipartFile());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        var cartItemDtoList = items.stream()
                .map(item -> new CartItemDto(item.getId(), 5))
                .toList();
        cartItemDtoList.stream()
                .forEach(dto -> cartService.addCartItem(dto, member.getEmail()));

        // when
        var cartDetailDtoList = cartService.getCartItemList(member.getEmail());

        // then
        assertThat(cartDetailDtoList.size()).isEqualTo(items.size());
    }

    public static <T, R> Function<T, R> wrap(ExceptionFunction<T, R> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}