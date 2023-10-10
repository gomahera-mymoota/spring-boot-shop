package com.kosa.shop.repository;

import com.kosa.shop.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
    List<Item> findByName(String name);

    List<Item> findByNameOrDetail(String name, String detail);

    @Query("SELECT i FROM Item i WHERE i.detail LIKE %:detail% ORDER BY i.price DESC")
    List<Item> findByDetail(@Param("detail") String detail);
}
