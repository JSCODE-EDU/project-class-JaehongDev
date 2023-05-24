package com.jaehong.projectclassjaehongdev.member.service;

import com.jaehong.projectclassjaehongdev.member.payload.response.MemberInquiryResponse;

public interface MemberInquiryService {
    MemberInquiryResponse execute(Long id);
}
