package com.fatec.vagasFatec.exceptions;

public class EntidadeJaExistenteException extends RuntimeException{
    public EntidadeJaExistenteException(){
        super("Entidade já existente");
    }

    public EntidadeJaExistenteException(String msg){
        super(msg);
    }
}
