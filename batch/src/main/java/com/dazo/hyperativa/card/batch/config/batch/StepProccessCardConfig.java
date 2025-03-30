package com.dazo.hyperativa.card.batch.config.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepProccessCardConfig {

    @Bean
    public Step stepProccessCard(JobRepository jobRepository,
                                 @Qualifier("appTransactionManager") PlatformTransactionManager platformTransactionManager,
                                 Tasklet processCardTasklet) {

        return new StepBuilder("stepProccessCard", jobRepository)
                .tasklet(processCardTasklet, platformTransactionManager)
                .build();
    }
}
