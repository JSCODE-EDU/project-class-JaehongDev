package com.jaehong.projectclassjaehongdev.post.service;


import com.jaehong.projectclassjaehongdev.global.domain.DomainExceptionCode;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostSwitchLikeServiceImpl implements PostSwitchLikeService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public void execute(Long memberId, Long postId) {
        var member = memberRepository.findById(memberId).orElseThrow(() -> DomainExceptionCode.MEMBER_ID_DID_NOT_EXISTS.create(memberId));
        var post = postRepository.findById(postId).orElseThrow(() -> DomainExceptionCode.POST_DID_NOT_EXISTS.create(postId));

        post.switchLike(member);
    }
}
