package com.jaehong.projectclassjaehongdev.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.utils.annotation.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@RepositoryTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원이_정상적으로_데이터베이스에_저장됩니다() {

        var email = "email@email.com";
        var password = "password";
        var member = memberRepository.save(Member.create(email, password));

        assertAll(() -> assertThat(member.getEmail()).isEqualTo(email),
                () -> assertThat(member.getPassword()).isEqualTo(password));
    }

}