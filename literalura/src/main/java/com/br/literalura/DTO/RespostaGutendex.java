package com.br.literalura.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RespostaGutendex(@JsonProperty("results") List<DadosLivro> resultados) { }