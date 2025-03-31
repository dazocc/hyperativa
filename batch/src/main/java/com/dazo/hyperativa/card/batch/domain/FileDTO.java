package com.dazo.hyperativa.card.batch.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {

    private Long id;
    private String name;
    private String status;
    private byte[] data;
    private byte[] dataError;
}
