package com.dazo.hyperativa.card.batch.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {

    private Long id;
    private byte[] data;
}
