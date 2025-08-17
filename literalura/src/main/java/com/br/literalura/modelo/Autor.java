package com.br.literalura.modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "autores", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String nome;

    private Integer anoNascimento;
    private Integer anoFalecimento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.LAZY)
    private Set<Livro> livros = new LinkedHashSet<>();

    public Autor() {}

    public Autor(String nome, Integer anoNascimento, Integer anoFalecimento) {
        this.nome = nome;
        this.anoNascimento = anoNascimento;
        this.anoFalecimento = anoFalecimento;
    }


    public String getNome() { return nome; }

    public Integer getAnoNascimento() { return anoNascimento; }
    public void setAnoNascimento(Integer anoNascimento) { this.anoNascimento = anoNascimento; }
    public Integer getAnoFalecimento() { return anoFalecimento; }
    public void setAnoFalecimento(Integer anoFalecimento) { this.anoFalecimento = anoFalecimento; }
    public Set<Livro> getLivros() { return livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Autor that)) return false;
        return Objects.equals(id, that.id) || Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Objects.requireNonNullElse(nome, ""));
    }
}

