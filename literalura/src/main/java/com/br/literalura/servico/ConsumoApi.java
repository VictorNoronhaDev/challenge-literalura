package com.br.literalura.servico;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsumoApi {
    private final HttpClient cliente = HttpClient.newHttpClient();

    public String buscarLivrosPorTitulo(String titulo) {
        try {
            String consulta = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            URI uri = URI.create("https://gutendex.com/books/?search=" + consulta);
            HttpRequest requisicao = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> resposta = cliente.send(requisicao, HttpResponse.BodyHandlers.ofString());
            return resposta.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Falha ao consultar Gutendex", e);
        }
    }
}