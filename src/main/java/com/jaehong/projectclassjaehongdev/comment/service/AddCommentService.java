package com.jaehong.projectclassjaehongdev.comment.service;

import com.jaehong.projectclassjaehongdev.comment.payload.AddCommentRequest;

public interface AddCommentService {
    void execute(AddCommentRequest addCommentRequest, Long writerId);
}
