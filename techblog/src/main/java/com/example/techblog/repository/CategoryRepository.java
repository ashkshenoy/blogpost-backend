package com.example.techblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techblog.model.Category;


public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findByName(String name);
}
