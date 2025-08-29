package com.example.webhooksql.service;

import com.example.webhooksql.model.GenerateWebhookResponse;
import com.example.webhooksql.model.FinalQueryRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    // Your RegNo is EVEN → Question 2 SQL query
    private static final String FINAL_SQL_QUERY =
            "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, " +
            "COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
            "FROM EMPLOYEE e1 " +
            "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
            "LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT " +
            "AND e2.DOB > e1.DOB " +
            "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME " +
            "ORDER BY e1.EMP_ID DESC";

    public void executeTask() {
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Generate webhook
        String requestJson = """
                {
                  "name": "Dedeepya Penumaka",
                  "regNo": "22BCE1894",
                  "email": "penumakadedeepya@gmail.com"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<GenerateWebhookResponse> response = restTemplate.exchange(
                GENERATE_WEBHOOK_URL,
                HttpMethod.POST,
                entity,
                GenerateWebhookResponse.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("Failed to get webhook response");
        }

        String webhookUrl = response.getBody().getWebhook();
        String accessToken = response.getBody().getAccessToken();

        // Step 2: Submit final SQL query
        FinalQueryRequest finalQuery = new FinalQueryRequest(FINAL_SQL_QUERY);

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        // postHeaders.setBearerAuth(accessToken);
        postHeaders.add("Authorization", accessToken);

        HttpEntity<FinalQueryRequest> postEntity = new HttpEntity<>(finalQuery, postHeaders);

        ResponseEntity<String> finalResponse = restTemplate.exchange(
                webhookUrl,
                HttpMethod.POST,
                postEntity,
                String.class
        );

        System.out.println("Final submission response: " + finalResponse.getBody());
    }
}
