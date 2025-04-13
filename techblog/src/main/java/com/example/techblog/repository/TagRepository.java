package com.example.techblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techblog.model.Tag;


public interface TagRepository extends JpaRepository<Tag, Long>{
	Optional<Tag> findByName(String name);
}
