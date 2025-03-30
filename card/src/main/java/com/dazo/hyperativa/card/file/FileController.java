package com.dazo.hyperativa.card.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    public static final String HAS_ROLE_APP = "hasRole('APP')";

    private final FileService fileService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize(HAS_ROLE_APP)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> upload(@RequestPart MultipartFile file) {
        fileService.createFile(file);
        return ResponseEntity.ok().build();
    }
}
