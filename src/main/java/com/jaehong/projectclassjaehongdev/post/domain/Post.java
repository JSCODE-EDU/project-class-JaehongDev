package com.jaehong.projectclassjaehongdev.post.domain;


import com.jaehong.projectclassjaehongdev.post.domain.policy.PostValidationPolicy;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 첫 번째 고민 정적 팩토리 메소드로 객체를 생성할때 정해진 규칙을 사용하는 것과 명확한 의미 전달중 어느 것을 선택해야 할까?
    public static Post createNewPost(String title, String content) {
        var post = new Post(title, content);
        PostValidationPolicy.validateAll(post);
        return post;
    }

    public Post updatePost(String editTitle, String editContent) {
        var updatePost = new Post(this.id, editTitle, editContent);
        PostValidationPolicy.validateAll(updatePost);
        return updatePost;
    }
}
