package com.jaehong.projectclassjaehongdev.comment.controller;


import com.jaehong.projectclassjaehongdev.comment.payload.AddCommentRequest;
import com.jaehong.projectclassjaehongdev.comment.service.AddCommentService;
import com.jaehong.projectclassjaehongdev.global.authentication.annotation.MemberId;
import com.jaehong.projectclassjaehongdev.global.authentication.annotation.Secured;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final AddCommentService addCommentService;

    @Secured
    @PostMapping()
    public ResponseEntity<Void> createComment(
            @MemberId Long memberId,
            @RequestBody AddCommentRequest commentRequest
    ) {
        this.addCommentService.execute(commentRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}