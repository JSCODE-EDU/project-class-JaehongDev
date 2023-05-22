package com.jaehong.projectclassjaehongdev.global.resolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jaehong.projectclassjaehongdev.global.authentication.annotation.MemberId;
import com.jaehong.projectclassjaehongdev.global.authentication.annotation.Secured;
import com.jaehong.projectclassjaehongdev.jwt.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("인증 Resolver가 정상적으로 컨트롤러에서 처리되는지 테스트")
@WebMvcTest({SampleController.class, TokenService.class})
public class MemberIdArgumentResolverIntegrateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Test
    void 애노테이션이_존재하지_않는_인증_컨트롤러에_요청하면_에러응답을_받습니다() throws Exception {
        mockMvc.perform(get("/test"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 애노테이션이_존재하지만_헤더가_없는_경우_에러가_발생한다() throws Exception {
        mockMvc.perform(get("/test2"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 헤더의_형태가_잘못된경우() throws Exception {
        mockMvc.perform(get("/test2").header("Authorization", "XXX"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 유효하지_않는_토큰의_경우_에러_응답이_발생한다() throws Exception {
        //만료된 토큰 생성
        var token = tokenService.issuedToken(1L, -1);
        mockMvc.perform(get("/test2").header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 정상적인_토큰의_경우_SUCCESS_응답이_발생한다() throws Exception {
        var token = tokenService.issuedToken(1L, 3600 * 10000);
        mockMvc.perform(get("/test2").header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUCCESS"));
    }


}

@RestController
class SampleController {

    @GetMapping("/test")
    public ResponseEntity<String> getInformation(@MemberId Long id) {
        return ResponseEntity.ok("SUCCESS");
    }

    @Secured
    @GetMapping("/test2")
    public ResponseEntity<String> getInformation2(@MemberId Long id) {
        return ResponseEntity.ok("SUCCESS");
    }
}
