package com.jaehong.projectclassjaehongdev.member.service;

import com.jaehong.projectclassjaehongdev.member.payload.request.SignInRequest;
import com.jaehong.projectclassjaehongdev.member.payload.response.SignInResponse;

public interface SignInService {

    SignInResponse execute(SignInRequest request);
}
