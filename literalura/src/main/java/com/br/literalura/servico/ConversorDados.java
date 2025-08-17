package com.br.literalura.servico;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversorDados {
    private final ObjectMapper mapeador;

    public ConversorDados() {
        this.mapeador = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapeador.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Falha ao converter JSON", e);
        }
    }
}