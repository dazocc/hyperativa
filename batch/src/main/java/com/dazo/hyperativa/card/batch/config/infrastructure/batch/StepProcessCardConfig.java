package com.dazo.hyperativa.card.batch.config.infrastructure.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepProcessCardConfig {

    @Bean
    public Step stepProcessCard(JobRepository jobRepository,
                                @Qualifier("appTransactionManager") PlatformTransactionManager platformTransactionManager,
                                Tasklet processCardTasklet) {

        return new StepBuilder("stepProcessCard", jobRepository)
                .tasklet(processCardTasklet, platformTransactionManager)
                .build();
    }
}
