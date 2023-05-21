package com.jaehong.projectclassjaehongdev.member.controller;


import com.jaehong.projectclassjaehongdev.global.authentication.annotation.MemberId;
import com.jaehong.projectclassjaehongdev.global.authentication.annotation.Secured;
import com.jaehong.projectclassjaehongdev.member.payload.response.MemberInquiryResponse;
import com.jaehong.projectclassjaehongdev.member.service.MemberInquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberInquiryService memberInquiryService;

    @Secured
    @GetMapping("/me")
    public ResponseEntity<MemberInquiryResponse> getMe(@MemberId Long memberId) {
        return ResponseEntity.ok(memberInquiryService.execute(memberId));
    }
}
