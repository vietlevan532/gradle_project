package com.globaltechjsc.vanvietle.gradle_project.service.dto;

import lombok.Data;

@Data
public class BlogDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
}
