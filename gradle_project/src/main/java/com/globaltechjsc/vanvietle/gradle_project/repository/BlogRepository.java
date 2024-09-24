package com.globaltechjsc.vanvietle.gradle_project.repository;

import com.globaltechjsc.vanvietle.gradle_project.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findById(Long id);
    void deleteById(Long id);
}
