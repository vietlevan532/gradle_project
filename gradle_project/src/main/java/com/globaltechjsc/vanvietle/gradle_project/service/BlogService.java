package com.globaltechjsc.vanvietle.gradle_project.service;

import com.globaltechjsc.vanvietle.gradle_project.domain.Blog;
import com.globaltechjsc.vanvietle.gradle_project.domain.User;
import com.globaltechjsc.vanvietle.gradle_project.repository.BlogRepository;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.BlogDTO;
import com.globaltechjsc.vanvietle.gradle_project.service.es.BlogDocService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogService {

    Logger log = LoggerFactory.getLogger(BlogService.class);

    private final BlogRepository blogRepository;

    private final BlogDocService blogDocumentService;

    public List<BlogDTO> getAllBlogs() {
        log.debug("Getting all blogs");
        return blogRepository.findAll().stream()
                .map(this::blogToBlogDTO)
                .collect(Collectors.toList());
    }

    public Optional<Blog> getBlogById(Long id) {
        log.debug("Getting blog by id {}", id);
        return blogRepository.findById(id);
    }

    public BlogDTO createBlog(BlogDTO blogRequest, User user) {
        log.debug("Creating blog {}", blogRequest);
        var newBlog = Blog.builder()
                .title(blogRequest.getTitle())
                .content(blogRequest.getContent())
                .user(user)
                .author(user.getUsername())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy")))
                .updatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy")))
                .build();
        blogRepository.save(newBlog);

        blogDocumentService.saveBlogDoc(newBlog);

        return this.blogToBlogDTO(newBlog);
    }

    public BlogDTO updateBlog(BlogDTO blogRequest, User user) {
        log.debug("Updating blog {}", blogRequest);
        Blog blog = blogRepository
                .findById(blogRequest.getId())
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setAuthor(user.getUsername());
        blog.setUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy")));
        blogRepository.save(blog);
        return this.blogToBlogDTO(blog);
    }

    public void deleteBlog(Long id, User user) {
        log.debug("Delete blog {}?", user.getFirstName());
        blogRepository.deleteById(id);
    }

    private BlogDTO blogToBlogDTO(Blog blog) {
        return BlogDTO.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .author(blog.getAuthor())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }
}
