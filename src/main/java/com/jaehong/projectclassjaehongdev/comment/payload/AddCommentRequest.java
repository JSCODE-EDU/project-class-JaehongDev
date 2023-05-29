package com.jaehong.projectclassjaehongdev.comment.payload;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AddCommentRequest {
    private Long postId;
    private String content;
}
