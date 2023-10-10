package com.kosa.shop.controller;

import com.kosa.shop.dto.MemberFormDto;
import com.kosa.shop.domain.entity.Member;
import com.kosa.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password) {
        var memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);

        var member = Member.createMember(memberFormDto, passwordEncoder);

        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        // given
        var email = "test@email.com";
        var password = "1234";
        this.createMember(email, password);

        // when
        mockMvc.perform(formLogin()
                        .userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email)
                        .password(password))
                // then
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        // given
        var email = "test@email.com";
        var password = "1234";
        this.createMember(email, password);

        // when
        mockMvc.perform(formLogin()
                        .userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email)
                        .password("12345"))
                // then
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }
}