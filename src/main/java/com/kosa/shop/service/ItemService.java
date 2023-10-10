package com.kosa.shop.service;

import com.kosa.shop.dto.ItemFormDto;
import com.kosa.shop.dto.ItemImgDto;
import com.kosa.shop.entity.ItemImg;
import com.kosa.shop.repository.ItemImgRepository;
import com.kosa.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception {
        var item = itemFormDto.createItem();
        itemRepository.save(item);

        int index = 0;
        for (var itemImgFile : itemImgFileList) {
//            var itemImgId = new ItemImgId();
//            itemImgId.setItem(item);

            var itemImg = new ItemImg();
            itemImg.setItem(item);
//            itemImg.setItemImgId(itemImgId);

            if (index++ == 0 && StringUtils.hasLength(itemImgFile.getOriginalFilename())) {
                itemImg.setIsRepImg(true);
            }

            itemImgService.saveItemImg(itemImg, itemImgFile);
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemFormDto(Long itemId) {
        var itemImgs = itemImgRepository.findByItemIdOrderById(itemId);
        var itemImgDtos = new ArrayList<ItemImgDto>();

        for (var itemImg : itemImgs) {
            var itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtos.add(itemImgDto);
        }

        var item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        var itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtos);

        return itemFormDto;
    }
}

