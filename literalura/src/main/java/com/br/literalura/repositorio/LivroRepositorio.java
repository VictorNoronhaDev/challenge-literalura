package com.br.literalura.repositorio;

import com.br.literalura.modelo.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LivroRepositorio extends JpaRepository<Livro, Long> {

    @Query("select l from Livro l where l.idGutendex = :id")
    Optional<Livro> buscarPorIdGutendex(@Param("id") Long id);

    // Busca livros já com autores (evita LazyInitializationException no CLI)
    @Query("select distinct l from Livro l left join fetch l.autores a order by l.titulo asc")
    List<Livro> listarComAutoresOrdenado();

    @Query("select distinct l from Livro l left join fetch l.autores a where lower(l.codigoIdioma) = lower(:codigo) order by l.titulo asc")
    List<Livro> buscarPorCodigoIdiomaOrdenadoComAutores(@Param("codigo") String codigo);
}