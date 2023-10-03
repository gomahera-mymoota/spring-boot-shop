package com.kosa.shop.repository;

import com.kosa.shop.entity.ItemImg;
import com.kosa.shop.entity.id.ItemImgId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImg, ItemImgId> {
}
