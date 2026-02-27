package com.fatec.vagasFatec.exceptions;

public class RegraDeNegocioVioladaException extends  RuntimeException{
    public RegraDeNegocioVioladaException(){
        super("Regra de negocio violada ou inativa");
    }

    public RegraDeNegocioVioladaException(String msg){
        super(msg);
    }
}
