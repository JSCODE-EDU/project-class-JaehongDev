package com.jaehong.projectclassjaehongdev.comment.service;

import com.jaehong.projectclassjaehongdev.comment.domain.Comment;
import com.jaehong.projectclassjaehongdev.comment.payload.AddCommentRequest;
import com.jaehong.projectclassjaehongdev.comment.repository.CommentRepository;
import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AddCommentServiceImpl implements AddCommentService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public void execute(AddCommentRequest addCommentRequest, Long writerId) {
        var writer = memberRepository.findById(writerId).orElseThrow(() -> DomainExceptionCode.MEMBER_ID_DID_NOT_EXISTS.create(writerId));
        var post = postRepository.findById(addCommentRequest.getPostId())
                .orElseThrow(() -> DomainExceptionCode.POST_DID_NOT_EXISTS.create(addCommentRequest.getPostId()));

        var newComment = Comment.builder()
                .content(addCommentRequest.getContent())
                .post(post)
                .writer(writer)
                .build();

        commentRepository.save(newComment);
    }
}
