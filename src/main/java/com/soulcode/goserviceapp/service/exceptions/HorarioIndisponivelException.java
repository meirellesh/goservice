package com.soulcode.goserviceapp.service.exceptions;

public class HorarioIndisponivelException extends RuntimeException {
    public HorarioIndisponivelException () {super("Horário indisponível");}

    public HorarioIndisponivelException (String message) {super(message);}
}
