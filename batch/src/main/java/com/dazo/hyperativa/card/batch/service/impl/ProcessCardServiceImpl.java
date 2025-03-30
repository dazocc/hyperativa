package com.dazo.hyperativa.card.batch.service.impl;

import com.dazo.hyperativa.card.batch.domain.FileDTO;
import com.dazo.hyperativa.card.batch.domain.StatusType;
import com.dazo.hyperativa.card.batch.repository.CardRepository;
import com.dazo.hyperativa.card.batch.repository.FileRepository;
import com.dazo.hyperativa.card.batch.service.ProcessCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessCardServiceImpl implements ProcessCardService {

    private final FileRepository fileRepository;
    private final CardRepository cardRepository;

    @Override
    public RepeatStatus execute() {

            StatusType statusType = StatusType.COMPLETED;
            List<String> lineError = new ArrayList<>();

        log.info("Retrieve file pending");
        FileDTO filePending = fileRepository.findFirstFilePending();

        while (filePending != null) {
            log.info("Process file");
            statusType = processFile(filePending, lineError, statusType);

            byte[] dataError = processDataError(lineError);

            log.info("Update file complete/error");

            fileRepository.updateFileById(filePending.getId(), statusType.toString(), dataError);

            log.info("Retrieve file complete");

            filePending = fileRepository.findFirstFilePending();
        }

        return RepeatStatus.FINISHED;
    }

    private static byte[] processDataError(List<String> lineError) {

        byte[] dataError = null;

        if(!ObjectUtils.isEmpty(lineError)){
            String erros = String.join("\n", lineError);
            dataError = erros.getBytes(StandardCharsets.UTF_8);
        }

        return dataError;
    }

    private StatusType processFile(FileDTO filePending, List<String> lineError, StatusType statusType) {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(filePending.getData());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream))) {
            String linha;
            while ((linha = reader.readLine()) != null) {

                log.info("Process line: {}", linha);
                String lineType = linha.substring(0, 1).trim();

                if("C".equals(lineType)){
                    String number = linha.substring(7, Math.min(linha.length(), 28)).trim();

                    log.info("Verify exists card number: {}", number);
                    boolean exists = cardRepository.verifyExistsCardByNumber(number);

                    if(exists) {
                        log.info("Card number: {} already exists", number);
                        lineError.add(linha);
                        statusType = StatusType.ERROR;
                    }else{
                        log.info("Insert card number: {}", number);
                        cardRepository.insertCard(number);
                    }
                }else if("L".equals(lineType)){
                    break;
                }

            }
        } catch (IOException e) {
            log.error("Error ao process line file", e);
            statusType = StatusType.ERROR;
        }

        return statusType;
    }


}
