package com.jaehong.projectclassjaehongdev.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class TokenServiceImplTest {

    @Test
    void 토큰이_정상적으로_디코딩_된다() {
        var tokenService = new TokenServiceImpl();
        var token = tokenService.issuedToken(1L, 60 * 60 * 10000);
        var id = tokenService.getById(token);
        Assertions.assertThat(id).isEqualTo(1);
    }

    @Test
    void 기간이_만료된_토큰의_경우_토큰을_사용할_수_없는_토큰으로_표시된다() {
        var tokenService = new TokenServiceImpl();
        var token = tokenService.issuedToken(1L, -1); // 현재 시간보다 -1초 늦게 만듦
        Assertions.assertThat(tokenService.verifyToken(token)).isFalse();
    }

}