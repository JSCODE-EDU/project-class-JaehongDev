package com.jaehong.projectclassjaehongdev.jwt;

import org.junit.jupiter.api.Test;

class TokenServiceImplTest {

    @Test
    void 토큰_테스트() {
        var tokenService = new TokenServiceImpl();

        var token = tokenService.issuedToken("1", 1);

        System.out.println(token);

        var id = tokenService.getSubject(token);
        System.out.println(tokenService.verifyToken(token));
        System.out.println(id);
    }

}