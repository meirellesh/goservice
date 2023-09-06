package com.soulcode.goserviceapp.service.exceptions;

public class EnderecoNaoEncontradoException extends RuntimeException{
    public EnderecoNaoEncontradoException() {
        super("Errro ao encontrar encontrado.");
    }

    public EnderecoNaoEncontradoException(String message) {
        super(message);
    }
}