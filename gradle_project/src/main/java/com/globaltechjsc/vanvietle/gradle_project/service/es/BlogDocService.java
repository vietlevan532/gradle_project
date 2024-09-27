package com.globaltechjsc.vanvietle.gradle_project.service.es;

import com.globaltechjsc.vanvietle.gradle_project.domain.Blog;
import com.globaltechjsc.vanvietle.gradle_project.domain.es.BlogDoc;
import com.globaltechjsc.vanvietle.gradle_project.repository.es.BlogDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogDocService {

    private final BlogDocRepository blogDocRepository;

    public void saveBlogDoc(Blog blog) {
        BlogDoc blogDoc = new BlogDoc();
        blogDoc.setId(blog.getId());
        blogDoc.setTitle(blog.getTitle());
        blogDoc.setContent(blog.getContent());
        blogDoc.setAuthor(blog.getAuthor());

        blogDocRepository.save(blogDoc);
    }
}
