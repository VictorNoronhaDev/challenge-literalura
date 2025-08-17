package com.br.literalura.servico;

import com.br.literalura.DTO.*;
import com.br.literalura.modelo.Autor;
import com.br.literalura.modelo.Livro;
import com.br.literalura.repositorio.AutorRepositorio;
import com.br.literalura.repositorio.LivroRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LivroServico {
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConversorDados conversorDados = new ConversorDados();
    private final LivroRepositorio livroRepositorio;
    private final AutorRepositorio autorRepositorio;

    public LivroServico(LivroRepositorio livroRepositorio, AutorRepositorio autorRepositorio) {
        this.livroRepositorio = livroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    @Transactional
    public Optional<LivroResumo> importarTitulo(String tituloDigitado) {
        String json = consumoApi.buscarLivrosPorTitulo(tituloDigitado);
        RespostaGutendex resposta = conversorDados.obterDados(json, RespostaGutendex.class);
        if (resposta == null || resposta.resultados() == null || resposta.resultados().isEmpty()) return Optional.empty();

        String alvo = normalizar(tituloDigitado);
        DadosLivro escolhido = resposta.resultados().stream()
                .filter(dl -> dl.titulo() != null && normalizar(dl.titulo()).equals(alvo))
                .findFirst()
                .orElse(null);

        if (escolhido == null) return Optional.empty();

        Livro livro = livroRepositorio.buscarPorIdGutendex(escolhido.id())
                .orElseGet(() -> new Livro(escolhido.id(), escolhido.titulo(),
                        escolhido.contagemDownloads(), extrairIdioma(escolhido)));

        livro.setTitulo(escolhido.titulo());
        livro.setContagemDownloads(Optional.ofNullable(escolhido.contagemDownloads()).orElse(0));
        livro.setCodigoIdioma(extrairIdioma(escolhido));

        if (escolhido.autores() != null) {
            for (DadosAutor autorDados : escolhido.autores()) {
                if (autorDados == null || autorDados.nome() == null || autorDados.nome().isBlank()) continue;
                Autor autor = autorRepositorio.buscarPorNomeIgnoreCase(autorDados.nome())
                        .orElseGet(() -> new Autor(autorDados.nome(), autorDados.anoNascimento(), autorDados.anoFalecimento()));
                if (autor.getAnoNascimento() == null && autorDados.anoNascimento() != null) autor.setAnoNascimento(autorDados.anoNascimento());
                if (autor.getAnoFalecimento() == null && autorDados.anoFalecimento() != null) autor.setAnoFalecimento(autorDados.anoFalecimento());
                autor = autorRepositorio.save(autor);
                livro.adicionarAutor(autor);
            }
        }

        livroRepositorio.save(livro);

        List<String> nomes = livro.getAutores().stream()
                .map(Autor::getNome)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();

        LivroResumo resumo = new LivroResumo(
                livro.getTitulo(),
                livro.getCodigoIdioma(),
                Optional.ofNullable(livro.getContagemDownloads()).orElse(0),
                nomes
        );

        return Optional.of(resumo);
    }


    private String extrairIdioma(DadosLivro dados) {
        List<String> idiomas = dados.idiomas();
        if (idiomas == null || idiomas.isEmpty()) return "desconhecido";
        return Optional.ofNullable(idiomas.get(0)).orElse("desconhecido");
    }

    private static String normalizar(String s) {
        if (s == null) return "";
        String t = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[\\r\\n]", " ")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
        return t;
    }


    public List<Livro> listarLivros() { return livroRepositorio.listarComAutoresOrdenado(); }
    public List<Autor> listarAutores() { return autorRepositorio.buscarTodosComLivros(); }
    public List<Autor> autoresVivosNoAno(int ano) { return autorRepositorio.buscarVivosNoAnoComLivros(ano); }
    public List<Livro> livrosPorIdioma(String codigo) { return livroRepositorio.buscarPorCodigoIdiomaOrdenadoComAutores(codigo); }
}
