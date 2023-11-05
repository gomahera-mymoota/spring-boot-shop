package com.kosa.shop.service;

import com.kosa.shop.domain.entity.Member;
import com.kosa.shop.dto.MemberFormDto;
import com.kosa.shop.repository.MemberRepository;
import com.kosa.shop.util.Source;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        var memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void saveMemberTest() throws Exception {
        // given
        BiConsumer<Member, String> init = (m, s) -> {
            m.setEmail("test" + s + "@email.com");
            m.setName("홍길동" + s);
            m.setAddress("서울시 마포구 합정동");
            m.setPassword("1234");
        };

        var memberSource = new Source<>(Member.class, init);
        var member = memberSource.getOne();
        log.info("member info : {}", member);


        // when
        var savedMember = memberService.saveMember(member);

        // then
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(savedMember.getName()).isEqualTo(member.getName());
        assertThat(savedMember.getAddress()).isEqualTo(member.getAddress());
        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(savedMember.getRole()).isEqualTo(member.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        // given
        var member1 = createMember();
        var member2 = createMember();
        memberService.saveMember(member1);

        // when & then
        // AssertJ의 예외처리 테스트 사용
        assertThatThrownBy(() -> memberService.saveMember(member2))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("이미 가입된 회원입니다.");
    }
}