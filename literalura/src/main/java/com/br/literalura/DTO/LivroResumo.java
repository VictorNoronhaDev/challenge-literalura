package com.br.literalura.DTO;

import java.util.List;

public record LivroResumo(
        String titulo,
        String codigoIdioma,
        Integer contagemDownloads,
        List<String> autores
) { }