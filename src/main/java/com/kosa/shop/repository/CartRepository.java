package com.kosa.shop.repository;

import com.kosa.shop.domain.entity.Cart;
import com.kosa.shop.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberIdMember(Member member);   // Optional 타입으로 교체

}
