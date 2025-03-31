package com.dazo.hyperativa.card.batch.config.infrastructure.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobProcessCardConfig {

    public static final String JOB_NAME = "jobProcessCard";

    @Bean
    public Job jobProcessCard(JobRepository jobRepository, Step stepProcessCard) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(stepProcessCard)
                .build();
    }
}
