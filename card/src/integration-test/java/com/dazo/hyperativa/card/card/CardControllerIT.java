package com.dazo.hyperativa.card.card;

import com.dazo.hyperativa.card.exception.MessageEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.dazo.hyperativa.card.exception.MessageEnum.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("TEST-H2")
@AutoConfigureMockMvc
class CardControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void setDown(){
        cardRepository.deleteAll();
    }

    @Nested
    @DisplayName("Find Car By Number")
    class FindCardByNumber {

        @DisplayName("Must Find Card By Number with success")
        @Test
        @WithMockUser(username= "davison", roles = { "USER", "APP"})
        void mustFindCardByNumberWithSuccess() throws Exception {

            String number = "123456789";
            CardEntity cardEntity = new CardEntity(number);
            cardRepository.save(cardEntity);

            mockMvc.perform(get("/card/{number}", number)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(cardEntity.getId()));
        }

        @DisplayName("Must Try Find Card By Number with error, Access Denied")
        @Test
        @WithMockUser(username= "davison", roles = { "XPTO"})
        void mustTryFindCardByNumberWithErrorNotAuhorized() throws Exception {

            String number = "123456789";

            String mensagem = getMensagem(ACCESS_DENIED);

            mockMvc.perform(get("/card/{number}", number)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(mensagem))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

        @DisplayName("Must Try Find Card By Number with error, Card Number Not Exists")
        @Test
        @WithMockUser(username= "davison", roles = { "USER", "APP"})
        void mustTryFindCardByNumberWithErrorCardNumberNotExists() throws Exception {

            String number = "123456789";

            String mensagem = getMensagem(ERROR_CARD_NUMBER_NOT_FOUND);

            mockMvc.perform(get("/card/{number}", number)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(mensagem))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

    }

    @Nested
    @DisplayName("Create Card")
    class CreateCard {

        @DisplayName("Must Create Card with success")
        @Test
        @WithMockUser(username= "davison", roles = { "USER", "APP"})
        void mustCreateCardWithSuccess() throws Exception {

            String number = "123456789";
            CardEntity cardEntity = new CardEntity(number);

            String requestBody = objectMapper.writeValueAsString(cardEntity);
            mockMvc.perform(post("/card")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @DisplayName("Must Try Create Card with error, Access Denied")
        @Test
        @WithMockUser(username= "davison", roles = { "XPTO"})
        void mustTryCreateCardWithErrorNotAuhorized() throws Exception {

            String number = "123456789";
            CardEntity cardEntity = new CardEntity(number);

            String mensagem = getMensagem(ACCESS_DENIED);

            String requestBody = objectMapper.writeValueAsString(cardEntity);
            mockMvc.perform(post("/card")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(mensagem))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

        @DisplayName("Must Create Card with error, Card Number Already Exists")
        @Test
        @WithMockUser(username= "davison", roles = { "USER", "APP"})
        void mustTryCreateCardWithErrorNumberAlreadyExists() throws Exception {

            String number = "123456789";
            CardEntity cardEntity = new CardEntity(number);
            cardRepository.save(cardEntity);

            String mensagem = getMensagem(ERROR_CARD_NUMBER_ALREADY_EXISTS);

            String requestBody = objectMapper.writeValueAsString(cardEntity);
            mockMvc.perform(post("/card")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(mensagem))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

        @DisplayName("Must Create Card with error, Card Number Not Provided")
        @Test
        @WithMockUser(username= "davison", roles = { "USER", "APP"})
        void mustTryCreateCarddWithErrorNumberNotProvided() throws Exception {

            String number = "123456789";
            CardEntity cardEntity = new CardEntity(null);

            String mensagem = getMensagem(ERROR_CARD_NUMBER_NOT_PROVIDED);

            String requestBody = objectMapper.writeValueAsString(cardEntity);
            mockMvc.perform(post("/card")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
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