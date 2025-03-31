package com.dazo.hyperativa.card.batch.service.impl;

import com.dazo.hyperativa.card.batch.domain.FileDTO;
import com.dazo.hyperativa.card.batch.domain.StatusType;
import com.dazo.hyperativa.card.batch.repository.CardRepository;
import com.dazo.hyperativa.card.batch.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProcessCardServiceImplTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private ProcessCardServiceImpl processCardService;

    ProcessCardServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Test for Execute")
    @Nested
    class Execute {

        @Test
        @DisplayName("Must Execute with success")
        void mustExecuteWithSuccess() {

            FileDTO filePending = FileDTO.builder()
                    .id(1L)
                    .data("C8     4456897919999999".getBytes(StandardCharsets.UTF_8))
                    .build();

            when(fileRepository.findFirstFilePending())
                    .thenReturn(filePending)
                    .thenReturn(null);

            RepeatStatus repeatStatus = processCardService.execute();

            assertEquals(RepeatStatus.FINISHED, repeatStatus);
        }

        @Test
        @DisplayName("Must Execute with success, dont have File Pending")
        void mustExecuteWithSuccessDontHaveFilePending() {

            when(fileRepository.findFirstFilePending()).thenReturn(null);

            RepeatStatus repeatStatus = processCardService.execute();

            assertEquals(RepeatStatus.FINISHED, repeatStatus);
        }

        @Test
        @DisplayName("Must Execute with success, card number already exists")
        void mustExecuteWithSuccessCardNumberAlreadyExist() {

            FileDTO filePending = FileDTO.builder()
                    .id(1L)
                    .data("C8     4456897919999999".getBytes(StandardCharsets.UTF_8))
                    .build();

            when(fileRepository.findFirstFilePending())
                    .thenReturn(filePending)
                    .thenReturn(null);

            when(cardRepository.verifyExistsCardByNumber(any())).thenReturn(true);

            RepeatStatus repeatStatus = processCardService.execute();

            assertEquals(RepeatStatus.FINISHED, repeatStatus);
            verify(fileRepository).updateFileById(any(), eq(StatusType.ERROR.toString()), any());
        }

        @Test
        @DisplayName("Must Execute with success, Line Type is not C")
        void mustExecuteWithSuccessLineTypeIsNotC() {

            FileDTO filePending = FileDTO.builder()
                    .id(1L)
                    .data("L8     4456897919999999".getBytes(StandardCharsets.UTF_8))
                    .build();

            when(fileRepository.findFirstFilePending())
                    .thenReturn(filePending)
                    .thenReturn(null);

            RepeatStatus repeatStatus = processCardService.execute();

            assertEquals(RepeatStatus.FINISHED, repeatStatus);
        }

        @Test
        @DisplayName("Must Execute with success, Line Type is not L")
        void mustExecuteWithSuccessLineTypeIsNotL() {

            FileDTO filePending = FileDTO.builder()
                    .id(1L)
                    .data("X8     4456897919999999".getBytes(StandardCharsets.UTF_8))
                    .build();

            when(fileRepository.findFirstFilePending())
                    .thenReturn(filePending)
                    .thenReturn(null);

            RepeatStatus repeatStatus = processCardService.execute();

            assertEquals(RepeatStatus.FINISHED, repeatStatus);
        }

        @Test
        @DisplayName("Must Execute with success, Access Field Data of File throw IOException ")
        void mustExecuteWithSuccessAccessFieldDataFileThrowIOException() {

            FileDTO filePending = FileDTO.builder()
                    .id(1L)
                    .data("C8     4456897919999999".getBytes(StandardCharsets.UTF_8))
                    .build();

            when(fileRepository.findFirstFilePending())
                    .thenReturn(filePending)
                    .thenReturn(null);

            doThrow(new RuntimeException()).when(cardRepository).verifyExistsCardByNumber(any());

            RepeatStatus repeatStatus = processCardService.execute();

            assertEquals(RepeatStatus.FINISHED, repeatStatus);
            verify(fileRepository).updateFileById(any(), eq(StatusType.ERROR.toString()), any());
        }
    }
}