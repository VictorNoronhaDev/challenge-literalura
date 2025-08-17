package com.br.literalura.repositorio;

import com.br.literalura.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {

    @Query("select a from Autor a where lower(a.nome) = lower(:nome)")
    Optional<Autor> buscarPorNomeIgnoreCase(@Param("nome") String nome);

    // Listar autores já com livros
    @Query("select distinct a from Autor a left join fetch a.livros")
    List<Autor> buscarTodosComLivros();

    // Autores vivos em um ano, já com seus livros
    @Query("select distinct a from Autor a left join fetch a.livros where a.anoNascimento is not null and a.anoNascimento <= :ano and (a.anoFalecimento is null or a.anoFalecimento >= :ano)")
    List<Autor> buscarVivosNoAnoComLivros(@Param("ano") int ano);
}
