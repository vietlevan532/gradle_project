package com.globaltechjsc.vanvietle.gradle_project.service;

import com.globaltechjsc.vanvietle.gradle_project.domain.Blog;
import com.globaltechjsc.vanvietle.gradle_project.repository.BlogRepository;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.BlogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogService {

    Logger log = LoggerFactory.getLogger(BlogService.class);

    private final BlogRepository blogRepository;

    private static class  BlogResourceException extends RuntimeException {
        private BlogResourceException(String message) {super(message);}
    }

    @Autowired
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<BlogDTO> getAllBlogs() {
        log.info("Getting all blogs");
        return blogRepository.findAll().stream()
                .map(this::blogToBlogDTO)
                .collect(Collectors.toList());
    }

    public Optional<Blog> getBlogById(Long id) {
        log.info("Getting blog by id {}", id);
        return blogRepository.findById(id);
    }

    public BlogDTO createBlog(BlogDTO blogRequest) {
        log.info("Creating blog {}", blogRequest);
        Blog blog = new Blog();
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setAuthor(blogRequest.getAuthor());
        blog.setCreatedAt(blogRequest.getCreatedAt());
        blogRepository.save(blog);
        return this.blogToBlogDTO(blog);
    }

    public BlogDTO updateBlog(BlogDTO blogRequest) {
        log.info("Updating blog {}", blogRequest);
        Blog blog = blogRepository
                .findById(blogRequest.getId())
                .orElseThrow(() -> new BlogResourceException("Blog not found"));
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setAuthor(blogRequest.getAuthor());
        blog.setCreatedAt(blogRequest.getCreatedAt());
        blogRepository.save(blog);
        return this.blogToBlogDTO(blog);
    }

    public void deleteBlog(Long id) {
        log.info("Deleting blog {}", id);
        blogRepository.deleteById(id);
    }

    private BlogDTO blogToBlogDTO(Blog blog) {
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setContent(blog.getContent());
        blogDTO.setAuthor(blog.getAuthor());
        blogDTO.setCreatedAt(blog.getCreatedAt());
        return blogDTO;
    }
}
