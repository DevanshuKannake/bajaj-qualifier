package com.bajaj.submit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class SubmitRunner implements ApplicationRunner {

    private static final String WEBHOOK = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IkFEVDIzU09DQjAzNDEiLCJuYW1lIjoiRGV2YW5zaHUgVmlub2QgS2FubmFrZSIsImVtYWlsIjoiZGV2YW5zaHVrYW5uYWtlNDgwQGdtYWlsLmNvbSIsInN1YiI6IndlYmhvb2stdXNlciIsImlhdCI6MTc3NjY4MjQ2OSwiZXhwIjoxNzc2NjgzMzY5fQ.LVIH4DTM2V17hM6y2Bj7XHEiS7QYj8F_qgFj76ls9F4";

    private static final String SQL = "SELECT p.AMOUNT AS SALARY, " +
            "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
            "DATE_PART('year', AGE(CURRENT_DATE, e.DOB)) AS AGE, " +
            "d.DEPARTMENT_NAME " +
            "FROM PAYMENTS p " +
            "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
            "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
            "WHERE EXTRACT(DAY FROM p.PAYMENT_TIME) != 1 " +
            "ORDER BY p.AMOUNT DESC LIMIT 1";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", TOKEN);

        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", SQL);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(WEBHOOK, request, String.class);

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response: " + response.getBody());
    }
}
