package com.jaehong.projectclassjaehongdev.comment.domain;


import com.jaehong.projectclassjaehongdev.member.domain.Member;
import com.jaehong.projectclassjaehongdev.post.domain.Post;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "COMMENTS") // mysql comment와 겹침
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member writer;
    @ManyToOne
    @JoinColumn(name = "POSTS_Id")
    private Post post;


    @Builder
    private Comment(String content, Member writer, Post post) {
        this.content = content;
        this.writer = writer;
        this.post = post;
    }
}
