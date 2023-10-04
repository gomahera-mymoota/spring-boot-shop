package com.kosa.shop.service;

import com.kosa.shop.dto.ItemFormDto;
import com.kosa.shop.entity.ItemImg;
import com.kosa.shop.entity.id.ItemImgId;
import com.kosa.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception {
        var item = itemFormDto.createItem();
        itemRepository.save(item);

        int index = 0;
        for (var itemImgFile : itemImgFileList) {
            var itemImgId = new ItemImgId();
            itemImgId.setItem(item);

            var itemImg = new ItemImg();
            itemImg.setItemImgId(itemImgId);

            if (index == 0) {
                itemImg.setIsRepImg(true);
            }
            itemImgService.saveItemImg(itemImg, itemImgFile);
        }

        return item.getId();
    }
}

