package com.dazo.hyperativa.authentication;

import com.dazo.hyperativa.authentication.advice.MensageExceptionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.dazo.hyperativa.authentication.advice.AppExceptionHandler.USERNAME_OR_PASSWORD_INCORRECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("TEST-H2")
class LoginTestIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Nested
    @DisplayName("Post Login")
    class PostLogin {

        @Test
        @DisplayName("The objective of this test is retrieve a JWT with success")
        void mustRetriveTokenWithSuccess() {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("username","davison");
            map.add("password","senha");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = testRestTemplate.postForEntity("/login", request, String.class);
            String token = response.getBody();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(token);
        }

        @Test
        @DisplayName("The objective of this test is validate error return ")
        void mustRetriveTokenWithError() {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("username","XX");
            map.add("password","XX");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<MensageExceptionResponse> response = testRestTemplate.postForEntity("/login", request, MensageExceptionResponse.class);
            MensageExceptionResponse mensageExceptionResponse = response.getBody();
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotNull(mensageExceptionResponse);
            assertNotNull(mensageExceptionResponse.getTimestamp());
            assertEquals(mensageExceptionResponse.getMessage(), USERNAME_OR_PASSWORD_INCORRECT);
        }
    }


}
