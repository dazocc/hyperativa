package com.dazo.hyperativa.card.batch.config.infrastructure.batch;

import com.dazo.hyperativa.card.batch.executor.ProcessCardTasklet;
import com.dazo.hyperativa.card.batch.service.ProcessCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TaskletProcessCardConfig {

    private final ProcessCardService processCardService;

    @Bean
    public Tasklet processCardTasklet() {
        return new ProcessCardTasklet(processCardService);
    }
}
