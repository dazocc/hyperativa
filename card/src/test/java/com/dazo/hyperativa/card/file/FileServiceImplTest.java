package com.dazo.hyperativa.card.file;

import com.dazo.hyperativa.card.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static com.dazo.hyperativa.card.exception.MessageEnum.ERROR_FILE_NOT_PROVIDED;
import static com.dazo.hyperativa.card.exception.MessageEnum.ERROR_FILE_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    FileServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Test for Create File")
    class CreateFile {

        @Test
        @DisplayName("Must Create File with success")
        void mustCreateFileWithSuccess() {

            MultipartFile multipartFile = new MockMultipartFile("file", "teste.txt",
                    MediaType.APPLICATION_OCTET_STREAM_VALUE, "files".getBytes(StandardCharsets.UTF_8));

            fileService.createFile(multipartFile);

            verify(fileRepository).save(any());
        }

        @Test
        @DisplayName("Must Try Create File, but the file is empty then throw a exception")
        void mustCreateFileWithErrorFileNotProvided() {

            byte[] content = new byte [] {};

            MultipartFile multipartFile = new MockMultipartFile("file", "teste.txt",
                    MediaType.APPLICATION_OCTET_STREAM_VALUE, content);

            BusinessException businessException = assertThrows(BusinessException.class, () -> fileService.createFile(multipartFile));

            assertNotNull(businessException);
            assertEquals(businessException.getMessageBusiness(), ERROR_FILE_NOT_PROVIDED);
        }

        @Test
        @DisplayName("Must Try Create File, but the file type is not txt throw a exception")
        void mustCreateFileWithErrorFileTypeNotTXT() {

            MultipartFile multipartFile = new MockMultipartFile("file", "teste.xpto",
                    MediaType.APPLICATION_OCTET_STREAM_VALUE, "files".getBytes(StandardCharsets.UTF_8));

            BusinessException businessException = assertThrows(BusinessException.class, () -> fileService.createFile(multipartFile));

            assertNotNull(businessException);
            assertEquals(businessException.getMessageBusiness(), ERROR_FILE_TYPE);
        }
    }
}