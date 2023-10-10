package com.kosa.shop.repository;

import com.kosa.shop.domain.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Integer> {

//    List<ItemImg> findByItemId(Long itemId);
    List<ItemImg> findByItemIdOrderById(Long itemId);

}
