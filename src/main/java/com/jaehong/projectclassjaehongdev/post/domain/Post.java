package com.jaehong.projectclassjaehongdev.post.domain;


import com.jaehong.projectclassjaehongdev.post.domain.policy.PostValidationPolicy;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String content;

    // 시간을 저장할때 LocalDatetime 넣는 것과 YYYY-MM-dd로 넣는 방법 중에서 어떤 방법이 좋을까?
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Post(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 고민 정적 팩토리 메소드로 객체를 생성할때 정해진 규칙을 사용하는 것과 명확한 의미 전달중 어느 것을 선택해야 할까?
    public static Post createNewPost(String title, String content) {
        var post = new Post(title, content);
        PostValidationPolicy.validateAll(post);
        return post;
    }

    public Post updatePost(String editTitle, String editContent) {
        var updatePost = new Post(this.id, editTitle, editContent, createdAt);
        PostValidationPolicy.validateAll(updatePost);
        return updatePost;
    }
}
