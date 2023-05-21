package com.jaehong.projectclassjaehongdev.member.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Password {
    @Column(name = "password")
    private String value;

    private Password(String value) {
        this.value = StringUtils.trimAllWhitespace(value);
    }

    public static Password create(String password) {
        return new Password(password);
    }

}
