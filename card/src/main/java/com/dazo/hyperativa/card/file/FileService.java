package com.dazo.hyperativa.card.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void createFile(MultipartFile file);
}
