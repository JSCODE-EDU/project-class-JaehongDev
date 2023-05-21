package com.jaehong.projectclassjaehongdev.member.payload.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInResponse {
    private final String token;

    public static SignInResponse from(String token) {
        return new SignInResponse(token);
    }
}
