package com.globaltechjsc.vanvietle.gradle_project.repository.es;

import com.globaltechjsc.vanvietle.gradle_project.domain.es.BlogDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogDocRepository extends ElasticsearchRepository<BlogDoc, String> {
}
