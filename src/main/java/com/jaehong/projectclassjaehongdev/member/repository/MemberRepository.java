package com.jaehong.projectclassjaehongdev.member.repository;

import com.jaehong.projectclassjaehongdev.member.domain.Email;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);

}
