package com.dazo.hyperativa.card.file;

import com.dazo.hyperativa.card.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.dazo.hyperativa.card.exception.MessageEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public void createFile(MultipartFile file) {

        log.info("Validate file");
        validateFile(file);

        log.info("Create file");
        FileEntity fileEntity = createFileEntityByMultipartFile(file);

        log.info("Validate save");
        fileRepository.save(fileEntity);
    }

    private static FileEntity createFileEntityByMultipartFile(MultipartFile file) {

        try {

            return FileEntity.builder()
                    .name(file.getOriginalFilename())
                    .status(StatusType.PENDING)
                    .data(file.getBytes())
                    .build();

        } catch (IOException e) {
            log.error("Error I/O when getBytes file", e);
            throw new BusinessException(ERROR_500);
        }
    }

    private static void validateFile(MultipartFile file) {

        if(file.isEmpty()){
            log.error("File is empty");
            throw new BusinessException(ERROR_FILE_NOT_PROVIDED);
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if(!"txt".equalsIgnoreCase(extension)) {
            log.error("Invalid file extension");
            throw new BusinessException(ERROR_FILE_TYPE);
        }
    }
}
