package com.dazo.hyperativa.card.card;

import com.dazo.hyperativa.card.card.dto.CreateCardRequest;
import com.dazo.hyperativa.card.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.dazo.hyperativa.card.exception.MessageEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceImplTest {

    public static final String NUMBER = "123456789";

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    CardServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Test for Find Card By Number")
    class FindCardByNumber {

        @Test
        @DisplayName("Must Find Card By Number with success")
        void mustFindCardByNumberWithSuccess() {

            Long id = 1L;
            CardEntity cardEntity = CardEntity.builder().id(id).build();

            when(cardRepository.findCardByNumber(any())).thenReturn(Optional.of(cardEntity));

            Long idActual = cardService.findCardByNumber(NUMBER);

            assertEquals(id, idActual);
        }

        @Test
        @DisplayName("Must Try Find Car By Number, but the card number was not provided throw a exception")
        void mustFindCardByNumberWithErrorCardNumberNotProvided() {

            BusinessException businessException = assertThrows(BusinessException.class, () -> cardService.findCardByNumber(null));

            assertNotNull(businessException);
            assertEquals(businessException.getMessageBusiness(), ERROR_CARD_NUMBER_NOT_PROVIDED);
        }

        @Test
        @DisplayName("Must Try Find Car By Number, but the card number was not found throw a exception")
        void mustFindCardByNumberWithErrorCardNumberNotFound() {

            when(cardRepository.findCardByNumber(any())).thenReturn(Optional.empty());

            BusinessException businessException = assertThrows(BusinessException.class, () -> cardService.findCardByNumber(NUMBER));

            assertNotNull(businessException);
            assertEquals(businessException.getMessageBusiness(), ERROR_CARD_NUMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Test for Create Card")
    class CreateCard {

        @Test
        @DisplayName("Must Create Card with success")
        void mustCreateCardWithSuccess() {

            CreateCardRequest createCardRequest = new CreateCardRequest();
            createCardRequest.setNumber("123456789");

            cardService.createCard(createCardRequest);

            verify(cardRepository).save(any());
        }

        @Test
        @DisplayName("Must Try Create Card, but the card number was not provided throw a exception")
        void mustCreateCardWithErrorCardNumberNotProvided() {

            CreateCardRequest createCardRequest = new CreateCardRequest();

            BusinessException businessException = assertThrows(BusinessException.class, () -> cardService.createCard(createCardRequest));

            assertNotNull(businessException);
            assertEquals(businessException.getMessageBusiness(), ERROR_CARD_NUMBER_NOT_PROVIDED);
        }

        @Test
        @DisplayName("Must Try Create Card, but the card number was not provided 2 throw a exception")
        void mustCreateCardWithErrorCardNumberNotProvided2() {

            BusinessException businessException = assertThrows(BusinessException.class, () -> cardService.createCard(null));

            assertNotNull(businessException);
            assertEquals(businessException.getMessageBusiness(), ERROR_CARD_NUMBER_NOT_PROVIDED);
        }

        @Test
        @DisplayName("Must Try Create Card, but the card number already exists throw a exception")
        void mustCreateCardWithErrorCardNumberAlreadeExists() {

            CreateCardRequest createCardRequest = new CreateCardRequest();
            createCardRequest.setNumber("123456789");

            when(cardRepository.existsByNumber(any())).thenReturn(true);

            BusinessException businessException = assertThrows(BusinessException.class, () -> cardService.createCard(createCardRequest));

            assertNotNull(businessException);
            assertEquals(businessException.getMessageBusiness(), ERROR_CARD_NUMBER_ALREADY_EXISTS);
        }

    }
}