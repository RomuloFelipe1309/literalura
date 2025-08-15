package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNome(String nome);


    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.livros WHERE(a.anoDeFalecimento IS NULL OR a.anoDeFalecimento > :ano) AND a.anoDeFalecimento <= :ano")
    List<Autor> findAutoresVivosNoAnoComLivros(@Param("ano")int ano);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.livros")
    List<Autor> findAllComLivros();
}
