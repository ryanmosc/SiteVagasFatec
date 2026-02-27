package com.fatec.vagasFatec.exceptions;

public class DadosNaoEncontrados extends RuntimeException{
    public DadosNaoEncontrados(){
        super("Dados não encontrados");
    }

    public DadosNaoEncontrados(String msg){
        super(msg);
    }
}
