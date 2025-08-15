package com.alura.literalura.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ConsumoAPI {

    public String obterDados(String url){
        // Cria uma instância de HttpClient para realizar as solicitações HTTP.
        HttpClient client = HttpClient.newHttpClient();

        // Constrói uma solicitação HTTP GET com a URL proporcionada
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Envia a solicitação HTTP e obtem a resposta
        HttpResponse<String> response = null;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            // Lança um RunTimeException si ocorrer um erro de E/S
            throw new RuntimeException("Error de E/S ao obter dados da API", e);
        } catch (InterruptedException e) {
            // Lança um RuntimeException si a solicitação é interrompida
            throw new RuntimeException("A solicitação foi interrompida.", e);
        }

        // Obtem o corpo da resposta em formato de sequência de texto
        String json = response.body();
        return json;
    }
}
