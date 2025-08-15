package com.alura.literalura.repository;

import com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Busca por título ignorando maiúsculas/minúsculas
    Optional<Livro> findByTituloIgnoreCase(String titulo);

    // Busca todos os livros de um idioma (case sensitive)
    List<Livro> findByIdioma(String idioma);

    // Busca todos os livros de um idioma (case insensitive)
    List<Livro> findByIdiomaIgnoreCase(String idioma);
}