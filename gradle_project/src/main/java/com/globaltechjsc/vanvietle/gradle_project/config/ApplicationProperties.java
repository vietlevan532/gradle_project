package com.globaltechjsc.vanvietle.gradle_project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Gradle Project.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
}
