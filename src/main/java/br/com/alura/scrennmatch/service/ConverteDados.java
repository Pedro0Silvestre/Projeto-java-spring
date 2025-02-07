package br.com.alura.scrennmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados{
    private ObjectMapper mapper = new ObjectMapper(); // objeto mapeador do jackson


    @Override
    public <T> T obtenerDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);//usado para json
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
