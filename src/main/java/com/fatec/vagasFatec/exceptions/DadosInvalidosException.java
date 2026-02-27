package com.fatec.vagasFatec.exceptions;

public class DadosInvalidosException extends RuntimeException{
    public DadosInvalidosException(){
        super("Dados invalidos ou faltantes");
    }

    public DadosInvalidosException (String msg){
        super(msg);
    }
}
