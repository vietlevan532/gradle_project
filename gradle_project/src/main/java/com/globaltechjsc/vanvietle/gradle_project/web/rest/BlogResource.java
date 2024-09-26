package com.globaltechjsc.vanvietle.gradle_project.web.rest;

import com.globaltechjsc.vanvietle.gradle_project.domain.Blog;
import com.globaltechjsc.vanvietle.gradle_project.domain.User;
import com.globaltechjsc.vanvietle.gradle_project.service.BlogService;
import com.globaltechjsc.vanvietle.gradle_project.service.UserService;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.BlogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final UserService userService;

    @Autowired
    public BlogResource(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
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
        log.debug("REST request to create new Blog : {}", blog);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserLogin(login);
        blogService.createBlog(blog, user);
        return ResponseEntity
                .created(new URI("/api/v1/new-blog/" + blog.getId()))
                .body(blog);
    }

    @PutMapping("/update")
    public ResponseEntity<BlogDTO> updateBlog(@RequestBody BlogDTO blogRequest) {
        log.debug("REST request to update Blog : {}", blogRequest);
        Blog checkingBlog = blogService
                .getBlogById(blogRequest.getId())
                .orElseThrow(() -> new BlogResourceException("Blog with id " + blogRequest.getId() + " not found"));

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserLogin(login);

        if (checkingBlog.getAuthor().equals(user.getUsername())) {
            CacheControl cacheControl = CacheControl
                    .maxAge(0, TimeUnit.SECONDS)
                    .cachePrivate()
                    .mustRevalidate();
            blogService.updateBlog(blogRequest, user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(cacheControl)
                    .body(blogRequest);
        }else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBlog(@RequestBody BlogDTO blogRequest) {
        log.debug("REST request to delete Blog : {}", blogRequest);
        Blog checkingBlog = blogService
                .getBlogById(blogRequest.getId())
                .orElseThrow(() -> new BlogResourceException("Blog with id " + blogRequest.getId() + " not found"));

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserLogin(login);

        if (checkingBlog.getAuthor().equals(user.getUsername())) {
            blogService.deleteBlog(blogRequest.getId(), user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Deleted Blog " + blogRequest.getTitle() + "!");
        }else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("This request is not authorized, please authenticate before proceed!");
        }
    }
}
