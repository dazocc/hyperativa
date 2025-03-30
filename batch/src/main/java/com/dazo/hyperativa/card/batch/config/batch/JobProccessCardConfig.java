package com.dazo.hyperativa.card.batch.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobProccessCardConfig {

    public static final String JOB_NAME = "jobProccessCard";

    @Bean
    public Job jobProccessCard(JobRepository jobRepository, Step stepProccessCard) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(stepProccessCard)
                .build();
    }
}
