package com.jaehong.projectclassjaehongdev.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.payload.request.PostSearch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    void 게시글_조건_범위_테스트() {

        //  title이 포함된 게시글 10개 생성
        postRepository.saveAllAndFlush(
                IntStream.range(1, 11).mapToObj(index -> Post.createNewPost("title" + index, "content" + index)).collect(Collectors.toList()));
        // jaehongDev가 포함된 게시글 5개 생성
        postRepository.saveAllAndFlush(IntStream.range(1, 6).mapToObj(index -> Post.createNewPost("jaehongDev" + index, "content" + index)).collect(
                Collectors.toList()));
        // jscode 가 포함된 게시글 115개 생성
        postRepository.saveAllAndFlush(IntStream.range(1, 116).mapToObj(index -> Post.createNewPost("jscode" + index, "content" + index)).collect(
                Collectors.toList()));

        Assertions.assertAll(
                () -> assertThat(postRepository.count()).isEqualTo(130),
                () -> assertThat(postRepository.findBy(PostSearch.builder().title("title").build()).size()).isEqualTo(10),
                () -> assertThat(postRepository.findBy(PostSearch.builder().title("jaehongDev").build()).size()).isEqualTo(5),
                () -> assertThat(postRepository.findBy(PostSearch.builder().title("jscode").build()).size()).isEqualTo(100)
        );
    }

    @Test
    void 게시글_키워드_검색이_빈_값이면_전체_검색() {
        postRepository.saveAllAndFlush(
                IntStream.range(1, 6).mapToObj(index -> Post.createNewPost("title" + index, "content" + index)).collect(Collectors.toList()));

        assertThat(postRepository.findBy(PostSearch.builder().build()).size()).isEqualTo(5);

    }

}