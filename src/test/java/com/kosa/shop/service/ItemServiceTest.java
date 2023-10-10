package com.kosa.shop.service;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.dto.ItemFormDto;
import com.kosa.shop.repository.ItemImgRepository;
import com.kosa.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {
        var multipartFileList = new ArrayList<MultipartFile>();

        for (int i = 0; i < 5; i++) {
            var path = "C:/Devs/shop/item/test/";
            var imageName = "image" + i + ".jpg";
            var multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        // given
        var itemFormDto = new ItemFormDto();
        itemFormDto.setName("테스트 상품");
        itemFormDto.setSellStatus(ItemSellStatus.SELL);
        itemFormDto.setDetail("테스트 상품입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        var multipartFileList = createMultipartFiles();
        var itemId = itemService.saveItem(itemFormDto, multipartFileList);

        // when
        var itemImgList = itemImgRepository.findByItemIdOrderById(itemId);
        var item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(item.getName()).isEqualTo(itemFormDto.getName());
        assertThat(item.getSellStatus()).isEqualTo(itemFormDto.getSellStatus());
        assertThat(item.getDetail()).isEqualTo(itemFormDto.getDetail());
        assertThat(item.getPrice()).isEqualTo(itemFormDto.getPrice());
        assertThat(item.getStockNumber()).isEqualTo(itemFormDto.getStockNumber());
        assertThat(itemImgList.get(0).getOriImgName()).isEqualTo(multipartFileList.get(0).getOriginalFilename());
    }
}
