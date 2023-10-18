package com.kosa.shop.service;

import com.kosa.shop.constant.Role;
import com.kosa.shop.domain.entity.Member;
import com.kosa.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService extends DefaultOAuth2UserService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    private void validateDuplicateMember(@NotNull Member member) {
        var foundMember = memberRepository.findByEmail(member.getEmail());
        if (foundMember.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var member = memberRepository.findByEmail(email);

        var result = member.orElseThrow(() -> new UsernameNotFoundException(email));     // Optional 활용

        return User.builder()
                .username(result.getEmail())
                .password(result.getPassword())
                .roles(result.getRole().toString())
                .build();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var attributes = super.loadUser(userRequest).getAttributes();
        log.info("ATTR INFO : {}", attributes);

        var oauthType = userRequest.getClientRegistration().getRegistrationId();
        var kakao = (Map<String, Object>) attributes.get("kakao_account");
        var email = kakao.get("email").toString();
        var name = ((Map<String, Object>) kakao.get("profile")).get("nickname").toString();
        log.info("LOGIN SUCCESS : {}, {} FROM {}", email, name, oauthType);

        var member = memberRepository.findByEmail(email)
                .map(m -> updateMember(m, name))
                .orElseGet(() -> createSocialMember(email, name));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
                kakao,
//                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
                "email"
        );
    }

    private Member updateMember(Member member, String name) {
        member.setName(name);

        return member;
    }

    private Member createSocialMember(String email, String name) {
        var member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setRole(Role.SOCIAL);

        return saveMember(member);
    }

}
