package com.example.webhooksql;

import com.example.webhooksql.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebhookSqlApplication implements CommandLineRunner {

    private final WebhookService webhookService;

    public WebhookSqlApplication(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebhookSqlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        webhookService.executeTask();
    }
}
