package com.example.techblog.controller;

import com.example.techblog.dto.*;
import com.example.techblog.service.BlogPostService;
import com.example.techblog.service.LikeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BlogPostController.class)
class BlogPostControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private BlogPostService postService;
    @MockBean private LikeService likeService;

    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "ash")
    void shouldCreatePostSuccessfully() throws Exception {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("Test Title");
        request.setContent("Test content");

        BlogPostDto responseDto = new BlogPostDto();
        responseDto.setTitle("Test Title");

        Mockito.when(postService.createPost(Mockito.any(), Mockito.eq("ash")))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void shouldGetAllPosts() throws Exception {
        Mockito.when(postService.getAllPosts(null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/posts"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
