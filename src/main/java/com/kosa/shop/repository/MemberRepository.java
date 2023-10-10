package com.kosa.shop.repository;

import com.kosa.shop.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);     // 반환타입을 Optional로 변경

}
