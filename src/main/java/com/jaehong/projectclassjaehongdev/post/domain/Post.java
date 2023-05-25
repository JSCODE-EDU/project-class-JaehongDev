package com.jaehong.projectclassjaehongdev.post.domain;


import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.post.domain.policy.PostValidationPolicy;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 1000)
    private String content;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // mapping
    // 연관관계를 매핑해주는 방법과
    // 단순하게 id를 가지는 방법중에서 어는 것이 더 좋은 방법일까?

    @ManyToOne
    private Member writer;

    private Post(Long id, String title, String content, Member member, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        PostValidationPolicy.validateAll(this);
    }


    private Post(String title, String content, Member writer) {
        this(null, title, content, writer, null);
    }

    // 고민 정적 팩토리 메소드로 객체를 생성할때 정해진 규칙을 사용하는 것과 명확한 의미 전달중 어느 것을 선택해야 할까?
    public static Post create(String title, String content, Member writer) {
        return new Post(title, content, writer);
    }

    public Post update(String editTitle, String editContent) {
        return new Post(this.id, editTitle, editContent, writer, createdAt);
    }
}
