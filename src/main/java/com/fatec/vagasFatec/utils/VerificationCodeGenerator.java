package com.fatec.vagasFatec.utils;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
//Sempre colocar uma anotação de @Service, nestas classes que precisam mexer com componentes, e se aonde você for usar tiver uma anotation do Lombok sem service ele cai
public class VerificationCodeGenerator {

    public static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final SecureRandom RANDOM = new SecureRandom();
    public static final int CODE_LENGHT = 6;

    public  String gerarCodigoValidacao(){
        StringBuilder stringBuilder = new StringBuilder(CODE_LENGHT);

        for (int i = 0; i < CODE_LENGHT; i++){
            int index = RANDOM.nextInt(CHARS.length());
            stringBuilder.append(CHARS.charAt(index));

        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
