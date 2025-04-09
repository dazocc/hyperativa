package com.dazo.hyperativa.card.card;

import com.dazo.hyperativa.card.card.dto.CreateCardRequest;
import com.dazo.hyperativa.card.card.dto.FindCardByNumberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    public static final String HAS_ROLE_USER = "hasRole('USER')";
    private final CardService cardService;

    @PreAuthorize(HAS_ROLE_USER)
    @Cacheable(value = "findCardByNumber", key = "#number")
    @GetMapping("/{number}")
    @ResponseStatus(HttpStatus.OK)
    public FindCardByNumberResponse findCardByNumber(@PathVariable("number") String number) {
        Long id = cardService.findCardByNumber(number);
        return new FindCardByNumberResponse(id);
    }

    @PreAuthorize(HAS_ROLE_USER)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createCard(@RequestBody CreateCardRequest createCardRequest) {
        cardService.createCard(createCardRequest);
        return ResponseEntity.ok().build();
    }
}
