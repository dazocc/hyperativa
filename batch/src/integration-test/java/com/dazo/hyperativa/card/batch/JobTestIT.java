package com.dazo.hyperativa.card.batch;

import com.dazo.hyperativa.card.batch.domain.FileDTO;
import com.dazo.hyperativa.card.batch.domain.StatusType;
import com.dazo.hyperativa.card.batch.repository.CardRepository;
import com.dazo.hyperativa.card.batch.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("TEST-H2")
@SpringBatchTest
class JobTestIT {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job jobProcessCard;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void setUp(){
        fileRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Test
    @DisplayName("Must Process Card with success")
    void mustProcessCard() throws Exception {

        FileDTO filePending = FileDTO.builder()
                .id(1L)
                .name("teste")
                .status(StatusType.PENDING.toString())
                .data("C1     4456897922969999\nC2     4456897922969998\nC3     4456897922969997".getBytes(StandardCharsets.UTF_8))
                .build();

        fileRepository.insertFile(filePending);

        JobParameters jobParameters = new JobParametersBuilder().addString("executionDate", Instant.now().toString()).toJobParameters();

        jobLauncherTestUtils.setJob(jobProcessCard);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        FileDTO file = fileRepository.findFileById(filePending.getId());

        assertNotNull(file);
        assertEquals(StatusType.COMPLETED.toString(), file.getStatus());
        assertNull(file.getDataError());

        assertTrue(cardRepository.verifyExistsCardByNumber("4456897922969999"));
        assertTrue(cardRepository.verifyExistsCardByNumber("4456897922969998"));
        assertTrue(cardRepository.verifyExistsCardByNumber("4456897922969997"));
    }

    @Test
    @DisplayName("Must Process Card with success not exists file")
    void mustProcessCardNotExistsFile() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder().addString("executionDate", Instant.now().toString()).toJobParameters();

        jobLauncherTestUtils.setJob(jobProcessCard);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Test
    @DisplayName("Must Process Card with success card number already exists file set ERROR")
    void mustProcessCarCardNumberAlreadyExists() throws Exception {

        FileDTO filePending = FileDTO.builder()
                .id(1L)
                .name("teste")
                .status(StatusType.PENDING.toString())
                .data("C1     4456897922969999\nC2     4456897922969999".getBytes(StandardCharsets.UTF_8))
                .build();

        fileRepository.insertFile(filePending);

        JobParameters jobParameters = new JobParametersBuilder().addString("executionDate", Instant.now().toString()).toJobParameters();

        jobLauncherTestUtils.setJob(jobProcessCard);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        FileDTO file = fileRepository.findFileById(filePending.getId());

        assertNotNull(file);
        assertEquals(StatusType.ERROR.toString(), file.getStatus());
        assertNotNull(file.getDataError());

        assertTrue(cardRepository.verifyExistsCardByNumber("4456897922969999"));
    }
}