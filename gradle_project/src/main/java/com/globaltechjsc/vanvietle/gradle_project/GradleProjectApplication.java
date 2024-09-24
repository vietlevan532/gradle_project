package com.globaltechjsc.vanvietle.gradle_project;

import com.globaltechjsc.vanvietle.gradle_project.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class GradleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradleProjectApplication.class, args);
	}

}
