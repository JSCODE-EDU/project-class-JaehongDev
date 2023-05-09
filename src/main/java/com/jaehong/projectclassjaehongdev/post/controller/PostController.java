package com.jaehong.projectclassjaehongdev.post.controller;

import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.service.PostCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostCreateService postCreateService;

    @PostMapping()
    public ResponseEntity<PostCreateResponse> createNewPost(@RequestBody PostCreateRequest postCreateRequest) {
        return ResponseEntity.ok(postCreateService.execute(postCreateRequest));
    }
}
