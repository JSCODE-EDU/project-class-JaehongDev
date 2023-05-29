package com.jaehong.projectclassjaehongdev.post.domain;


import com.jaehong.projectclassjaehongdev.comment.domain.Comment;
import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.post.domain.policy.PostValidationPolicy;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "POSTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String content;
    // mapping
    // 연관관계를 매핑해주는 방법과
    // 단순하게 id를 가지는 방법중에서 어는 것이 더 좋은 방법일까?
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private Member writer;

    @OneToMany
    @JoinColumn(name = "POSTS_ID")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "post_like",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id"))
    private List<Member> likedPost;

    private Post(Long id, String title, String content, Member writer, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
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

    public Post update(PostEditor postEditor) {
        this.confirmWriter(postEditor.getWriter());
        return new Post(postEditor.getTitle(), postEditor.getContent(), postEditor.getWriter());
    }

    public void confirmWriter(Member member) {
        PostValidationPolicy.validateWriter(this, member);
    }

    public void switchLike(Member member) {
        var likedPost = this.getLikedPost();
        // 좋아요 삭제
        if (likedPost.contains(member)) {
            likedPost.remove(member);
            return;
        }
        // 좋아요 등록
        likedPost.add(member);
    }
}
