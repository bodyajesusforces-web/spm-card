package com.example.spcard;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CardService {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    // Номер по умолчанию до первого запроса
    private static String cardNumber = "Загрузка...";

    // Метод обновления данных с сайта
    public static void updateCard(String apiUrl, String token) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(5))
                .GET();

        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }

        client.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    // Если сайт отдает сырой номер или JSON
                    cardNumber = body.trim();
                })
                .exceptionally(e -> {
                    cardNumber = "Ошибка связи";
                    return null;
                });
    }

    public static String getCardNumber() {
        return cardNumber;
    }

    public static void setManualCardNumber(String number) {
        cardNumber = number;
    }
}
