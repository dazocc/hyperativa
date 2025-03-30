package com.dazo.hyperativa.card.batch.service;

import org.springframework.batch.repeat.RepeatStatus;

public interface ProcessCardService {
    RepeatStatus execute();
}
