package com.jaehong.projectclassjaehongdev.member.controller;


import com.jaehong.projectclassjaehongdev.member.payload.request.SignInRequest;
import com.jaehong.projectclassjaehongdev.member.payload.request.SignUpRequest;
import com.jaehong.projectclassjaehongdev.member.payload.response.SignInResponse;
import com.jaehong.projectclassjaehongdev.member.service.SignInService;
import com.jaehong.projectclassjaehongdev.member.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SignUpService signUpService;
    private final SignInService signInService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
        signUpService.execute(signUpRequest);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> sign(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(signInService.execute(signInRequest));
    }

}
