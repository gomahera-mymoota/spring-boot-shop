package com.kosa.shop.repository;

import com.kosa.shop.entity.ItemImg;
import com.kosa.shop.entity.id.ItemImgId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, ItemImgId> {

    List<ItemImg> findByItemImgIdItemIdOrderByCreatedByAsc(Long itemId);

}
