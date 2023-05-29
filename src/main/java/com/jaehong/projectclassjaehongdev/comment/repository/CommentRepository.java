package com.jaehong.projectclassjaehongdev.comment.repository;

import com.jaehong.projectclassjaehongdev.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}