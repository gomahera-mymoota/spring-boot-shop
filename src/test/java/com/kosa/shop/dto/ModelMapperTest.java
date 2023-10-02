package com.kosa.shop.dto;

import com.kosa.shop.entity.Item;
import com.kosa.shop.entity.ItemImg;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ModelMapperTest {

    @Test
    @DisplayName("ModelMapper 성공 테스트")
    public void modelMapperTest() {
        // given
        var modelMapper = new ModelMapper();

        // when
        modelMapper.createTypeMap(ItemImg.class, ItemImgDto.class);

        modelMapper.createTypeMap(ItemFormDto.class, Item.class);
        modelMapper.createTypeMap(Item.class, ItemFormDto.class);

        // then
        modelMapper.validate();
    }
}