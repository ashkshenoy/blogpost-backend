package com.example.techblog.repository;

import com.example.techblog.model.BlogPost;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface BlogPostRepository extends JpaRepository<BlogPost, Long>, JpaSpecificationExecutor<BlogPost> {
	List<BlogPost> findByCategory_Name(String category);

    // Fetch all posts with a specific tag
    List<BlogPost> findByTagsId(Long tagId);
}
