package com.dazo.hyperativa.card.batch.repository;

import com.dazo.hyperativa.card.batch.domain.FileDTO;

public interface FileRepository {
    FileDTO findFirstFilePending();
    FileDTO findFileById(Long id);
    void updateFileById(Long id, String status, byte[] dataError);
    void insertFile(FileDTO filePending);
    void deleteAll();
}
