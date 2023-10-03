package com.kosa.shop.service;

import com.kosa.shop.domain.ShopFile;
import com.kosa.shop.entity.ItemImg;
import com.kosa.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        var oriImgName = itemImgFile.getOriginalFilename();
        var shopFile = new ShopFile(oriImgName);

        shopFile.upload(itemImgLocation, itemImgFile.getBytes());
        var imgName = shopFile.getSavedFileName();
        var imgUrl = StringUtils.hasLength(imgName) ? "/images/item/" + imgName : "";

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
}
