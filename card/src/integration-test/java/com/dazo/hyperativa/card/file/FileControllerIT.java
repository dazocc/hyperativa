package com.dazo.hyperativa.card.file;

import com.dazo.hyperativa.card.exception.MessageEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.dazo.hyperativa.card.exception.MessageEnum.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("TEST-H2")
@AutoConfigureMockMvc
class FileControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void setDown() {
        fileRepository.deleteAll();
    }

    @Nested
    @DisplayName("Create File")
    class CreateFile {

        @DisplayName("Must Create File with success")
        @Test
        @WithMockUser(username = "davison", roles = {"USER", "APP"})
        void mustCreateFileWithSuccess() throws Exception {

            MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                    "testFile.txt", "text/plain", "testFile.txt".getBytes());

            mockMvc.perform(multipart("/file")
                            .file(mockMultipartFile)
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @DisplayName("Must Try Create File with error, Access Denied")
        @Test
        @WithMockUser(username = "davison", roles = {"XPTO"})
        void mustTryCreateFileWithErrorNotAuhorized() throws Exception {

            MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                    "testFile.txt", "text/plain", "teste".getBytes());
            String mensagem = getMensagem(ACCESS_DENIED);

            mockMvc.perform(multipart("/file")
                            .file(mockMultipartFile)
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(mensagem))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

        @DisplayName("Must Try Create File with error, File Type is not Txt")
        @Test
        @WithMockUser(username = "davison", roles = {"USER", "APP"})
        void mustTryCreateFileWithErrorFileTypeNotTxt() throws Exception {

            MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                    "testFile.xpto", "text/plain", "teste".getBytes());

            String mensagem = getMensagem(ERROR_FILE_TYPE);

            mockMvc.perform(multipart("/file")
                            .file(mockMultipartFile)
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(mensagem))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

        @DisplayName("Must Try Create File with error, File not provided")
        @Test
        @WithMockUser(username = "davison", roles = {"USER", "APP"})
        void mustTryCreateFileWithErrorFileNotProvided() throws Exception {

            MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                    "testFile.xpto", "text/plain", new byte []{});

            String mensagem = getMensagem(ERROR_FILE_NOT_PROVIDED);

            mockMvc.perform(multipart("/file")
                            .file(mockMultipartFile)
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(mensagem))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }
    }

    private String getMensagem(MessageEnum mensagem) {
        return messageSource.getMessage(mensagem.toString(), null, LocaleContextHolder.getLocale());
    }
}