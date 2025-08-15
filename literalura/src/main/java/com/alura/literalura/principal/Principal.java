package com.alura.literalura.principal;

import com.alura.literalura.dto.AutorDTO;
import com.alura.literalura.dto.LivroDTO;
import com.alura.literalura.dto.RespostaLivrosDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Livro;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvertendoDados;
import com.alura.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Autowired
    private LivroService livroService;
    @Autowired
    private AutorService autorService;
    @Autowired
    private ConsumoAPI consumoAPI;
    @Autowired
    private ConvertendoDados convertendoDados;

    private static final String BASE_URL = "https://gutendex.com/books/";

    // ------------------- MÉTODO DE DETALHES -------------------
    private void mostrarDetalhes(LivroDTO livroDTO) {
        System.out.println("-------LIVRO--------");
        System.out.println("Título: " + livroDTO.getTitulo());

        String autores = livroDTO.getAutores().isEmpty()
                ? "Desconhecido"
                : livroDTO.getAutores().stream()
                .map(AutorDTO::getNome)
                .collect(Collectors.joining(", "));
        System.out.println("Autor(es): " + autores);

        String idioma = livroDTO.getIdiomas().isEmpty()
                ? "Desconhecido"
                : livroDTO.getIdiomas().get(0);
        System.out.println("Idioma: " + idioma);

        System.out.println("Número de downloads: " + livroDTO.getNumeroDeDownloads());
    }

    // ------------------- MENU PRINCIPAL -------------------
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Sejam bem vindos ao Literalura!\n ");
        int opcao;

        do {
            System.out.println("\n--- LITERALURA ---");
            System.out.println("1 - Buscar livro por título");
            System.out.println("2 - Listar livros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos em um ano");
            System.out.println("5 - Listar livros por idioma");
            System.out.println("0 - Sair");
            System.out.print("Selecione uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir newline

            switch (opcao) {
                case 1:
                    buscarLivroPorTitulo(scanner);
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos(scanner);
                    break;
                case 5:
                    listarLivrosPorIdioma(scanner);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    System.out.printf("Obrigado por acessar o Literalura! ");
                    break;
                default:
                    System.out.println("Opção não válida. Tente de novo.");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // ------------------- OPÇÕES DO MENU -------------------

    private void buscarLivroPorTitulo(Scanner scanner) {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine();

        try {
            String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String json = consumoAPI.obterDados(BASE_URL + "?search=" + encodedTitulo);
            RespostaLivrosDTO respostaLivrosDTO = convertendoDados.obterDados(json, RespostaLivrosDTO.class);

            List<LivroDTO> livrosDTO = respostaLivrosDTO.getLivros();

            if (livrosDTO.isEmpty()) {
                System.out.println("Livro não encontrado na API");
                return;
            }

            boolean livroRegistrado = false;

            for (LivroDTO livroDTO : livrosDTO) {
                if (livroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                    Optional<Livro> livroExistente = livroService.obterLivroPorTitulo(titulo);

                    if (livroExistente.isPresent()) {
                        System.out.println("Detalhe: Chave (titulo)=(" + titulo + ") já existe.");
                        System.out.println("Não se pode registrar o mesmo livro mais de uma vez.");
                        livroRegistrado = true;
                        break;
                    } else {
                        Livro livro = new Livro();
                        livro.setTitulo(livroDTO.getTitulo());
                        livro.setIdioma(livroDTO.getIdiomas().isEmpty() ? "Desconhecido" : livroDTO.getIdiomas().get(0));
                        livro.setNumeroDeDownloads(livroDTO.getNumeroDeDownloads());

                        // Pesquisar ou criar o Autor
                        AutorDTO primeiroAutorDTO = livroDTO.getAutores().isEmpty() ? null : livroDTO.getAutores().get(0);
                        Autor autor = null;

                        if (primeiroAutorDTO != null) {
                            autor = autorService.obterAutorPorNome(primeiroAutorDTO.getNome())
                                    .orElseGet(() -> {
                                        Autor novoAutor = new Autor();
                                        novoAutor.setNome(primeiroAutorDTO.getNome());
                                        novoAutor.setAnoDeNascimento(primeiroAutorDTO.getAnoDeNascimento());
                                        novoAutor.setAnoDeFalecimento(primeiroAutorDTO.getAnoDeFalecimento());
                                        return autorService.criarAutor(novoAutor);
                                    });
                        }

                        livro.setAutor(autor);

                        // Salvar livro
                        livroService.criarLivro(livro);
                        System.out.println("Livro registrado: " + livro.getTitulo());
                        mostrarDetalhes(livroDTO);

                        livroRegistrado = true;
                        break;
                    }
                }
            }

            if (!livroRegistrado) {
                System.out.println("Não consegui encontrar um livro exatamente com esse título: " + titulo);
            }

        } catch (Exception e) {
            System.out.println("Erro ao obter dados da API: " + e.getMessage());
        }
    }

    private void listarLivros() {
        livroService.listarLivros().forEach(livro -> {
            System.out.println("-------LIVRO--------");
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Autor desconhecido"));
            System.out.println("Idioma: " + livro.getIdioma());
            System.out.println("Número de downloads: " + livro.getNumeroDeDownloads());
        });
    }

    private void listarAutores() {
        autorService.listarAutores().forEach(autor -> {
            System.out.println("----------AUTOR------------");
            System.out.println("Autor: " + autor.getNome());
            System.out.println("Data de nascimento: " + autor.getAnoDeNascimento());
            System.out.println("Data de falecimento: " + autor.getAnoDeFalecimento());

            String livros = autor.getLivros().stream()
                    .map(Livro::getTitulo)
                    .collect(Collectors.joining(", "));
            System.out.println("Livros: [" + livros + "]");
        });
    }

    private void listarAutoresVivos(Scanner scanner) {
        System.out.print("Digite o ano do(s) autor(es) que deseja pesquisar: ");
        int ano = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autoresVivos = autorService.listarAutoresVivosNoAno(ano);

        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor vivo foi encontrado no ano " + ano);
        } else {
            autoresVivos.forEach(autor -> {
                System.out.println("----------AUTOR-------------");
                System.out.println("Autor: " + autor.getNome());
                System.out.println("Data de nascimento: " + autor.getAnoDeNascimento());
                System.out.println("Data de falecimento: " + (autor.getAnoDeFalecimento() != null ? autor.getAnoDeFalecimento() : "Vivo"));
                System.out.println("Número de livros: " + autor.getLivros().size());
            });
        }
    }

    private void listarLivrosPorIdioma(Scanner scanner) {
        System.out.print("Digite o idioma (es, en, fr, pt): ");
        String idioma = scanner.nextLine();

        if (!List.of("es", "en", "fr", "pt").contains(idioma.toLowerCase())) {
            System.out.println("Idioma não válido. Tente novamente.");
            return;
        }

        livroService.listarLivrosPorIdioma(idioma).forEach(livro -> {
            System.out.println("-------LIVRO--------");
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Autor desconhecido"));
            System.out.println("Idioma: " + livro.getIdioma());
            System.out.println("Número de downloads: " + livro.getNumeroDeDownloads());
        });
    }
}


