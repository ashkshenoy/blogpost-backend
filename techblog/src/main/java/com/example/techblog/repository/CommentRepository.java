package com.example.techblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techblog.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByPostId(Long Id);
}
