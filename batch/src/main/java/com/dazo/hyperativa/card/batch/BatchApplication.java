package com.dazo.hyperativa.card.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class BatchApplication implements CommandLineRunner {

    private final Job jobProcessCard;
    private final JobLauncher jobLauncher;

    @Value("${spring.task.scheduling.enabled}")
    private boolean scheduleEnabled;

    public static void main(String[] args) {
        new SpringApplicationBuilder(BatchApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override

    public void run(String... args) throws Exception {
        executeJob();
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void executeJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

        if(scheduleEnabled){
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("executionDate", Instant.now().toString())
                    .toJobParameters();

            jobLauncher.run(jobProcessCard, jobParameters);
        }
    }
}
