package com.globaltechjsc.vanvietle.gradle_project.web.rest;

import com.globaltechjsc.vanvietle.gradle_project.domain.Blog;
import com.globaltechjsc.vanvietle.gradle_project.service.BlogService;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.BlogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api/v1/blogs")
public class BlogResource {

    private final Logger log = LoggerFactory.getLogger(BlogResource.class);

    private final BlogService blogService;

    @Autowired
    public BlogResource(BlogService blogService) {
        this.blogService = blogService;
    }

    private static class  BlogResourceException extends RuntimeException {
        private BlogResourceException(String message) {super(message);}
    }

    @GetMapping("/")
    public ResponseEntity<List<BlogDTO>> getBlogs() {
        log.debug("REST request to get all blogs");
        List<BlogDTO> blogs = blogService.getAllBlogs();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(blogs);
    }

    @PostMapping("/create")
    public ResponseEntity<BlogDTO> createBlog(@RequestBody BlogDTO blog) throws URISyntaxException {
        log.debug("REST request to save Blog : {}", blog);
        blogService.createBlog(blog);
        return ResponseEntity
                .created(new URI("/api/v1/new-blog/" + blog.getId()))
                .body(blog);
    }

    @PutMapping("/update")
    public ResponseEntity<BlogDTO> updateBlog(@RequestBody BlogDTO blogRequest) {
        log.debug("REST request to update Blog : {}", blogRequest);
        Blog findBlog = blogService
                .getBlogById(blogRequest.getId())
                .orElseThrow(() -> new BlogResourceException("Blog with id " + blogRequest.getId() + " not found"));

        CacheControl cacheControl = CacheControl
                .maxAge(0, TimeUnit.SECONDS)
                .cachePrivate()
                .mustRevalidate();
        blogService.updateBlog(blogRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(cacheControl)
                .body(blogRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBlog(@RequestBody BlogDTO blogRequest) {
        log.debug("REST request to delete Blog : {}", blogRequest);
        Blog findBlog = blogService
                .getBlogById(blogRequest.getId())
                .orElseThrow(() -> new BlogResourceException("Blog with id " + blogRequest.getId() + " not found"));
        blogService.deleteBlog(blogRequest.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Deleted Blog " + blogRequest.getTitle() + "!");
    }
}
