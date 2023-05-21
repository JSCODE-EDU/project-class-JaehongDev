package com.jaehong.projectclassjaehongdev.member.domain;


import com.jaehong.projectclassjaehongdev.member.domain.policy.MemberValidationPolicy;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;


@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Password password;
    @Embedded
    private Email email;
    @CreationTimestamp
    private LocalDateTime createdAt;

    private Member(String email, String password) {
        this.email = Email.create(email);
        this.password = Password.create(password);

        MemberValidationPolicy.validate(this);
    }

    public static Member create(String email, String password) {
        return new Member(email, password);
    }

    public boolean comparePassword(String password) {
        return this.password.equals(Password.create(password));
    }

    public String getEmail() {
        return this.email.getValue();
    }

    public String getPassword() {
        return this.password.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return getId() != null && Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
