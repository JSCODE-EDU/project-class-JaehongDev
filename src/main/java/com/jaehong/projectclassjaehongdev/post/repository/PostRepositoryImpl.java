package com.jaehong.projectclassjaehongdev.post.repository;

import com.jaehong.projectclassjaehongdev.post.domain.Post;
import com.jaehong.projectclassjaehongdev.post.vo.PostSearchCondition;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public List<Post> findBy(PostSearchCondition postSearchCondition) {
        return entityManager.createQuery("select p from TestPost p where p.title like :searchTitle order by p.createdAt DESC", Post.class)
                .setParameter("searchTitle", "%" + postSearchCondition.getKeyword() + "%")
                .setFirstResult(0)
                .setMaxResults(postSearchCondition.getLimit())
                .getResultList();
    }
}
