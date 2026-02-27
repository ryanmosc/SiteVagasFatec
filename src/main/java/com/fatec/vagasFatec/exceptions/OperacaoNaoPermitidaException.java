package com.fatec.vagasFatec.exceptions;

public class OperacaoNaoPermitidaException extends RuntimeException{
    public OperacaoNaoPermitidaException(){
        super("Operação não permitida ou inválida");
    }

    public OperacaoNaoPermitidaException(String msg){
        super(msg);
    }
}
