package com.dazo.hyperativa.card.card;

import com.dazo.hyperativa.card.card.dto.CreateCardRequest;
import com.dazo.hyperativa.card.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import static com.dazo.hyperativa.card.exception.MessageEnum.*;


@Repository
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public Long findCardByNumber(String number) {

        if (ObjectUtils.isEmpty(number)) {
            log.error("Number is empty");
            throw new BusinessException(ERROR_CARD_NUMBER_NOT_PROVIDED);
        }

        log.info("Retrive card by number: {}", number);
        CardEntity cardEntity = cardRepository.findCardByNumber(number)
                .orElseThrow(() -> new BusinessException(ERROR_CARD_NUMBER_NOT_FOUND));

        return cardEntity.getId();
    }

    @Override
    public void createCard(CreateCardRequest createCardRequest) {

        if (ObjectUtils.isEmpty(createCardRequest) || ObjectUtils.isEmpty(createCardRequest.getNumber())) {
            log.error("Number is empty");
            throw new BusinessException(ERROR_CARD_NUMBER_NOT_PROVIDED);
        }

        boolean exitsCardNumber = cardRepository.existsByNumber(createCardRequest.getNumber());

        if(exitsCardNumber){
            log.error("Card number already exists");
           throw new BusinessException(ERROR_CARD_NUMBER_ALREADY_EXISTS);
        }

        CardEntity cardEntity = new CardEntity(createCardRequest.getNumber());

        log.info("Create card: {}", cardEntity);
        cardRepository.save(cardEntity);
    }
}
