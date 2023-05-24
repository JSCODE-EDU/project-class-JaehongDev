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
public class Email {
    @Column(name = "email", unique = true)
    private String value;

    private Email(String value) {
        this.value = StringUtils.trimAllWhitespace(value);
    }

    public static Email create(String value) {
        return new Email(value);
    }
}
