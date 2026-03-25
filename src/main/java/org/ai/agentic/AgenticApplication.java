package org.ai.agentic;

import org.springframework.ai.retry.autoconfigure.SpringAiRetryAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;

@SpringBootApplication(exclude = {SpringAiRetryAutoConfiguration.class})
public class AgenticApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgenticApplication.class, args);
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }
}
