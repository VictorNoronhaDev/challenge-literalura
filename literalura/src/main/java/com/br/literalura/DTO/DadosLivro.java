package com.br.literalura.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(
        Long id,
        @JsonProperty("title") String titulo,
        @JsonProperty("download_count") Integer contagemDownloads,
        @JsonProperty("languages") List<String> idiomas,
        @JsonProperty("authors") List<DadosAutor> autores
) { }