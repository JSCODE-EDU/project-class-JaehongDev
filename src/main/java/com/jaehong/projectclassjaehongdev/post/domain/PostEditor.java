package com.jaehong.projectclassjaehongdev.post.domain;

import com.jaehong.projectclassjaehongdev.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Builder
@RequiredArgsConstructor
public class PostEditor {
    private final String title;
    private final String content;
    private final Member writer;
}
