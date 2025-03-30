package com.dazo.hyperativa.card.file;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "files")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FileEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    private byte[] data;

    @Column(name = "data_error")
    private byte[] dataError;
}
