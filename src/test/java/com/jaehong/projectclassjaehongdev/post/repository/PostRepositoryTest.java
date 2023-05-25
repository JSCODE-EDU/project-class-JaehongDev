package com.jaehong.projectclassjaehongdev.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.member.repository.MemberRepository;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.vo.PostSearchCondition;
import com.jaehong.projectclassjaehongdev.utils.annotation.RepositoryTest;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    void 게시글이_정상적으로_데이터베이스에_저장됩니다() {
        var member = memberRepository.save(Member.create("email@email.com", "password"));
        var entity = postRepository.save(Post.create("-".repeat(10), "-".repeat(1000), member));

        assertThat(entity.getTitle()).isEqualTo("-".repeat(10));
        assertThat(entity.getContent()).isEqualTo("-".repeat(1000));
    }

    @Test
    void 게시글_조건_범위_테스트() {

        //  title이 포함된 게시글 10개 생성
        postRepository.saveAll(generateSampleData(10, generatePostEntity("title")));
        // jaehongDev가 포함된 게시글 5개 생성
        postRepository.saveAll(generateSampleData(5, generatePostEntity("jaehong")));
        // jscode 가 포함된 게시글 115개 생성
        postRepository.saveAll(generateSampleData(115, generatePostEntity("jscode")));

        Assertions.assertAll(
                () -> assertThat(postRepository.count()).isEqualTo(130),
                () -> assertThat(postRepository.findBy(PostSearchCondition.create("title")).size()).isEqualTo(10),
                () -> assertThat(postRepository.findBy(PostSearchCondition.create("jaehong")).size()).isEqualTo(5),
                () -> assertThat(postRepository.findBy(PostSearchCondition.create("jscode")).size()).isEqualTo(100)
        );
    }

    @Test
    void 게시글_키워드_검색이_빈_값이면_전체_검색() {
        var member = memberRepository.save(Member.create("email@email.com", "password"));
        postRepository.saveAllAndFlush(generateSampleData(5, (index) -> Post.create("title" + index, "content", member)));

        assertThat(postRepository.findBy(PostSearchCondition.createEmptyCondition()).size()).isEqualTo(5);

    }

    private IntFunction<Post> generatePostEntity(String title) {
        var member = memberRepository.save(Member.create("email@email.com", "password"));
        return (index) -> Post.create(title + index, "content" + index, member);
    }

    private List<Post> generateSampleData(int quantity, IntFunction<Post> createPostEntity) {
        return IntStream.range(1, quantity + 1)
                .mapToObj(createPostEntity)
                .collect(Collectors.toList());
    }
}