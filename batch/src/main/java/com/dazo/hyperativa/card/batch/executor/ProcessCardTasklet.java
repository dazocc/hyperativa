package com.dazo.hyperativa.card.batch.executor;

import com.dazo.hyperativa.card.batch.service.ProcessCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class ProcessCardTasklet implements Tasklet {

    private final ProcessCardService processCardService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        return processCardService.execute();
    }
}
