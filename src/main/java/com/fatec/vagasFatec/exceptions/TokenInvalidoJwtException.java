package com.fatec.vagasFatec.exceptions;

public class TokenInvalidoJwtException extends RuntimeException{
    public TokenInvalidoJwtException(){
        super("Token invalido ou violado");
    }
    public TokenInvalidoJwtException(String msg){
        super(msg);
    }
}
