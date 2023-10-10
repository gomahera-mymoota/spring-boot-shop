package com.kosa.shop.service;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.dto.ItemFormDto;
import com.kosa.shop.domain.entity.ItemImg;
import com.kosa.shop.repository.ItemImgRepository;
import com.kosa.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ItemImgServiceTest {
    @Autowired
    private ItemImgService itemImgService;
    @Autowired
    private ItemImgRepository itemImgRepository;
    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager em;

    MultipartFile getMultipartFile() throws Exception {
        var path = "C:/Devs/shop/item/test/";
        var imageName = "image0.jpg";
        var multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});

        return multipartFile;
    }
    @Test
    @DisplayName("아이템 이미지 저장 테스트")
    public void saveItemImgTest() throws Exception {
        // given
        var itemFormDto = new ItemFormDto();
        itemFormDto.setName("테스트 상품");
        itemFormDto.setSellStatus(ItemSellStatus.SELL);
        itemFormDto.setDetail("테스트 상품입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);
        var item = itemFormDto.createItem();
        itemRepository.save(item);

        var itemImg = new ItemImg();
        itemImg.setItem(item);
        var multipartFileList = getMultipartFile();

        // when
        itemImgService.saveItemImg(itemImg, multipartFileList);

        em.flush();
        em.clear();

        // then
        var savedItemImg = itemImgRepository.findByItemIdOrderById(item.getId());
        assertThat(savedItemImg.get(0).getId()).isEqualTo(itemImg.getId());
//        System.out.println(savedItemImg.get(0).getItemImgId());
    }
}