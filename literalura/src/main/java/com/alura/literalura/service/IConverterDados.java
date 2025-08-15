package com.alura.literalura.service;

public interface IConverterDados {

    <T> T obterDados(String Json, Class<T> classe);
}
