package com.jaehong.projectclassjaehongdev.post.service;

import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostDeleteServiceImpl implements PostDeleteService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void execute(Long postId, Long memberId) {
        // 서비스 계층의 책임은 여기에서 한정되는 것이 아니라고 생각이든다.
        // 게시글을 삭제할때 검증해야하는 요소는 다음과 같습니다.

        // 회원이 실제로 존재하는가
        // 게시글이 실제로 존재하는가
        // 작성자와 삭제하려고 하는 회원이 일치하는가

        var member = memberRepository.findById(memberId).orElseThrow(() -> DomainExceptionCode.MEMBER_ID_DID_NOT_EXISTS.create(memberId));
        var post = postRepository.findById(postId).orElseThrow(() -> DomainExceptionCode.POST_DID_NOT_EXISTS.create(postId));

        post.confirmWriter(member);
        postRepository.delete(post);
    }
}
