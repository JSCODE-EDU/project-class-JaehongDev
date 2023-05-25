package com.jaehong.projectclassjaehongdev.post.controller;

import com.jaehong.projectclassjaehongdev.global.authentication.annotation.MemberId;
import com.jaehong.projectclassjaehongdev.global.authentication.annotation.Secured;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostCreateRequest;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostEditRequest;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostCreateResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostEditResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostFindResponse;
import com.jaehong.projectclassjaehongdev.post.payload.response.PostsResponse;
import com.jaehong.projectclassjaehongdev.post.service.PostCreateService;
import com.jaehong.projectclassjaehongdev.post.service.PostDeleteService;
import com.jaehong.projectclassjaehongdev.post.service.PostEditService;
import com.jaehong.projectclassjaehongdev.post.service.PostFindService;
import com.jaehong.projectclassjaehongdev.post.service.PostsFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostCreateService postCreateService;
    private final PostEditService postEditService;
    private final PostDeleteService postDeleteService;
    private final PostFindService postFindService;
    private final PostsFindService postsFindService;


    @Secured
    @PostMapping
    public ResponseEntity<PostCreateResponse> createNewPost(@MemberId Long memberId, @RequestBody PostCreateRequest postCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postCreateService.execute(postCreateRequest, memberId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostEditResponse> updatePost(@RequestBody PostEditRequest postEditRequest, @PathVariable Long postId) {
        return ResponseEntity.ok(postEditService.execute(postId, postEditRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postDeleteService.execute(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostFindResponse> findPostBy(@PathVariable Long postId) {
        return ResponseEntity.ok(postFindService.execute(postId));
    }

    @GetMapping()
    public ResponseEntity<PostsResponse> findPostsByAll(@ModelAttribute PostSearch postSearch) {
        return ResponseEntity.ok(postsFindService.execute(postSearch));
    }
}
