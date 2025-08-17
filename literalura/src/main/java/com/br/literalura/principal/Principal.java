package com.br.literalura.principal;

import com.br.literalura.modelo.Autor;
import com.br.literalura.modelo.Livro;
import com.br.literalura.servico.LivroServico;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    private final LivroServico livroServico;
    private final Scanner scanner = new Scanner(System.in);

    public Principal(LivroServico livroServico) {
        this.livroServico = livroServico;
    }

    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n========= LITERALURA =========");
            System.out.println("1 - Buscar livro pelo título e registrar");
            System.out.println("2 - Listar livros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos em um determinado ano");
            System.out.println("5 - Listar livros por idioma");
            System.out.println("0 - Sair");
            System.out.print("Escolha a opção desejada: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> buscarESalvarPorTitulo();
                case 2 -> listarLivros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivosNoAno();
                case 5 -> listarLivrosPorIdioma();
                case 0 -> System.out.println("Encerrando. Até mais!");
                default -> System.out.println("Opção inválida.");
            }
        }
    }


    private void buscarESalvarPorTitulo() {
        System.out.print("Digite o título do livro: ");
        String termo = scanner.nextLine().trim();
        if (termo.isBlank()) { System.out.println("Título vazio."); return; }

        var opt = livroServico.importarTitulo(termo); // use a *instância* livroServico
        if (opt.isPresent()) {
            var r = opt.get();
            String autores = r.autores().isEmpty() ? "—" : String.join(", ", r.autores());
            System.out.printf(
                    "\n---- Livro -----\nTítulo: %s\nAutor: %s\nIdioma: %s\nDownloads: %d\n----------------------\n",
                    r.titulo(), autores, r.codigoIdioma(), r.contagemDownloads()
            );
        } else {
            System.out.println("Título não encontrado. \n");
        }
    }




private void listarLivros() {
    List<Livro> livros = livroServico.listarLivros();
    if (livros.isEmpty()) { System.out.println("Nenhum livro cadastrado."); return; }
    livros.stream()
            .sorted(Comparator.comparing(Livro::getTitulo, String.CASE_INSENSITIVE_ORDER))
            .forEach(l -> {
                String autores = l.getAutores().stream()
                        .map(Autor::getNome)
                        .sorted(String.CASE_INSENSITIVE_ORDER)
                        .collect(Collectors.joining(", "));
                System.out.printf("-------- Livro -------- \n- Título: %s \n Autor: %s \n Idioma: %s \n Downloads: %d%n ---------------- \n\n",
                        l.getTitulo(), autores.isBlank()?"—":autores, l.getCodigoIdioma(),
                        l.getContagemDownloads()==null?0:l.getContagemDownloads());
            });
}


private void listarAutores() {
    List<Autor> autores = livroServico.listarAutores();
    if (autores.isEmpty()) { System.out.println("Nenhum autor cadastrado."); return; }
    autores.stream()
            .sorted(Comparator.comparing(Autor::getNome, String.CASE_INSENSITIVE_ORDER))
            .forEach(a -> {
                String livros = a.getLivros().stream()
                        .map(Livro::getTitulo)
                        .sorted(String.CASE_INSENSITIVE_ORDER)
                        .collect(Collectors.joining(", "));
                System.out.printf("- Autor: %s \n Nascimento: %s \n Falecimento: %s \n Livros: %s%n ---------------- \n\n",
                        a.getNome(),
                        a.getAnoNascimento()==null?"?":a.getAnoNascimento().toString(),
                        a.getAnoFalecimento()==null?"?":a.getAnoFalecimento().toString(),
                        livros.isBlank()?"—":livros);
            });
}


private void listarAutoresVivosNoAno() {
    System.out.print("Digite o ano (ex.: 1900): ");
    String input = scanner.nextLine().trim();
    int ano;
    try { ano = Integer.parseInt(input); } catch (Exception e) { System.out.println("Ano inválido."); return; }
    List<Autor> vivos = livroServico.autoresVivosNoAno(ano);
    if (vivos.isEmpty()) { System.out.println("Nenhum autor encontrado vivo em " + ano + "."); return; }
    vivos.stream()
            .sorted(Comparator.comparing(Autor::getNome, String.CASE_INSENSITIVE_ORDER))
            .forEach(a -> {
                String livros = a.getLivros().stream()
                        .map(Livro::getTitulo)
                        .sorted(String.CASE_INSENSITIVE_ORDER)
                        .collect(Collectors.joining(", "));
                System.out.printf("\n %s \n (%s - %s) \n Livros: %s%n ",
                        a.getNome(),
                        a.getAnoNascimento()==null?"?":a.getAnoNascimento().toString(),
                        a.getAnoFalecimento()==null?"?":a.getAnoFalecimento().toString(),
                        livros.isBlank()?"—":livros);
            });
}


private void listarLivrosPorIdioma() {
    System.out.print("Digite o idioma (es/en/fr/pt): ");
    String codigo = scanner.nextLine().trim().toLowerCase();
    if (!(codigo.equals("es") || codigo.equals("en") || codigo.equals("fr") || codigo.equals("pt"))) {
        System.out.println("Idioma inválido. Use: es, en, fr, pt.");
        return;
    }
    List<Livro> livros = livroServico.livrosPorIdioma(codigo);
    if (livros.isEmpty()) {
        System.out.println("Não há livros cadastrados no idioma '" + codigo + "'.");
        return;
    }
    livros.forEach(l -> {
        String autores = l.getAutores().stream().map(Autor::getNome).sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.joining(", "));
        System.out.printf("\n Título: %s \n Idioma: %s \n Autor(es): %s \n Downloads: %d%n",
                l.getTitulo(), l.getCodigoIdioma(), autores.isBlank()?"—":autores,
                l.getContagemDownloads()==null?0:l.getContagemDownloads());
    });
}
}
