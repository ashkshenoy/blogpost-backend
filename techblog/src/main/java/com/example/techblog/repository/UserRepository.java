package com.example.techblog.repository;

import com.example.techblog.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	 User  findByUsername(String username);
}