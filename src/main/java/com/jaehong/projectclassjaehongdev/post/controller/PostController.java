package com.jaehong.projectclassjaehongdev.post.controller;

import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostEditRequest;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostEditResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;
import com.jaehong.projectclassjaehongdev.post.service.PostCreateService;
import com.jaehong.projectclassjaehongdev.post.service.PostDeleteService;
import com.jaehong.projectclassjaehongdev.post.service.PostEditService;
import com.jaehong.projectclassjaehongdev.post.service.PostFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostCreateService postCreateService;
    private final PostEditService postEditService;
    private final PostDeleteService postDeleteService;
    private final PostFindService postFindService;

    @PostMapping
    public ResponseEntity<PostCreateResponse> createNewPost(@RequestBody PostCreateRequest postCreateRequest) {
        return ResponseEntity.ok(postCreateService.execute(postCreateRequest));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostEditResponse> updatePost(@RequestBody PostEditRequest postEditRequest, @PathVariable Long postId) {
        return ResponseEntity.ok(postEditService.execute(postId, postEditRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postDeleteService.execute(postId);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostFindResponse> findPostBy(@PathVariable Long postId) {
        return ResponseEntity.ok(postFindService.execute(postId));
    }

}
