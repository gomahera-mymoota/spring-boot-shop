package com.kosa.shop.service;

import com.kosa.shop.domain.ShopFile;
import com.kosa.shop.domain.entity.ItemImg;
import com.kosa.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        var oriImgName = itemImgFile.getOriginalFilename();
        var shopFile = new ShopFile(oriImgName);

        shopFile.upload(itemImgLocation, itemImgFile.getBytes());
        var imgName = shopFile.getSavedFileName();
        var imgUrl = StringUtils.hasLength(imgName) ? "/images/item/" + imgName : "";

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(List<Long> itemImgIds, List<MultipartFile> itemImgFileList) {
        IntStream.range(0, itemImgIds.size())
                        .forEach(i ->
                                updateItemImg(itemImgIds.get(i), itemImgFileList.get(i))
                        );
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) {
        if (itemImgFile.isEmpty()) {
            return;
        }

        try {
            var savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            if (StringUtils.hasLength(savedItemImg.getImgName())) {
                var shopFile = new ShopFile(savedItemImg.getOriImgName());
                shopFile.delete(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            var oriImgName = itemImgFile.getOriginalFilename();
            var shopFile = new ShopFile(oriImgName);
            shopFile.upload(itemImgLocation, itemImgFile.getBytes());
            var imgName = shopFile.getSavedFileName();
            var imgUrl = StringUtils.hasLength(imgName) ? "/images/item/" + imgName : "";

            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
