package com.jaehong.projectclassjaehongdev.member.payload.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Builder
@Getter
public class MemberInquiryResponse {
    private final Long id;
    private final String email;
    private final LocalDateTime createdAt;
}
