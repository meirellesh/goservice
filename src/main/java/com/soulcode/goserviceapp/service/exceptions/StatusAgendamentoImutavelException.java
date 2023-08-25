package com.soulcode.goserviceapp.service.exceptions;

public class StatusAgendamentoImutavelException extends RuntimeException{

    public StatusAgendamentoImutavelException() {
        super("Status do Agendamento não pode ser alterado");
    }

    public StatusAgendamentoImutavelException(String message) {
        super(message);
    }
}
