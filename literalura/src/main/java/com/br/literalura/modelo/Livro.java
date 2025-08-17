package com.br.literalura.modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "livros", uniqueConstraints = @UniqueConstraint(columnNames = "id_gutendex"))
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_gutendex", nullable = false, unique = true)
    private Long idGutendex;

    @Column(name = "titulo", columnDefinition = "TEXT", nullable = false)
    private String titulo;

    @Column(name = "contagem_downloads")
    private Integer contagemDownloads;

    @Column(name = "codigo_idioma", length = 10)
    private String codigoIdioma; // principal (ex.: "en", "pt")

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "livros_autores",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new LinkedHashSet<>();

    public Livro() {}

    public Livro(Long idGutendex, String titulo, Integer contagemDownloads, String codigoIdioma) {
        this.idGutendex = idGutendex;
        this.titulo = titulo;
        this.contagemDownloads = contagemDownloads;
        this.codigoIdioma = codigoIdioma;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Integer getContagemDownloads() { return contagemDownloads; }
    public void setContagemDownloads(Integer contagemDownloads) { this.contagemDownloads = contagemDownloads; }
    public String getCodigoIdioma() { return codigoIdioma; }
    public void setCodigoIdioma(String codigoIdioma) { this.codigoIdioma = codigoIdioma; }
    public Set<Autor> getAutores() { return autores; }

    public void adicionarAutor(Autor a) {
        this.autores.add(a);
        a.getLivros().add(this);
    }
}
