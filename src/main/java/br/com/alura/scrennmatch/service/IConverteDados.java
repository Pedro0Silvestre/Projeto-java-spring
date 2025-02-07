package br.com.alura.scrennmatch.service;

public interface IConverteDados {
    <T> T obtenerDados(String json, Class<T> classe) ;
}
