package com.dazo.hyperativa.card.card;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "CARDS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CardEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    public CardEntity(String number) {
        this.number = number;
    }
}
