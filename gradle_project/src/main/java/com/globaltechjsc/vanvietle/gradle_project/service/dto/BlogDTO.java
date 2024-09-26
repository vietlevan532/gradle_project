package com.globaltechjsc.vanvietle.gradle_project.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
    private String updatedAt;
}
