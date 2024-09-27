package com.globaltechjsc.vanvietle.gradle_project.domain.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "blogs")
@Data
public class BlogDoc {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
    private String updatedAt;
}
