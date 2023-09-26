package com.kosa.shop.service;

import com.kosa.shop.entity.Member;
import com.kosa.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

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
}
