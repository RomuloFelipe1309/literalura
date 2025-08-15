package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LivroDTO {

    @JsonProperty("title")
    private String titulo;

    @JsonProperty("id")
    private int id;

    @JsonProperty("authors")
    private List<AutorDTO> autores;

    @JsonProperty("languages")
    private List<String> idiomas;

    @JsonProperty("download_count")
    private int numeroDeD0wnloads;


    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AutorDTO> getAutores() {
        return autores;
    }

    public void setAutores(List<AutorDTO> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public int getNumeroDeD0wnloads() {
        return numeroDeD0wnloads;
    }

    public void setNumeroDeD0wnloads(int numeroDeD0wnloads) {
        this.numeroDeD0wnloads = numeroDeD0wnloads;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getNumeroDeDownloads() {
        return numeroDeD0wnloads ;
    }
}
