package com.soulcode.goserviceapp.domain;

import com.soulcode.goserviceapp.domain.enums.StatusAgendamento;

import java.time.LocalDateTime;

public class MudancaDeStatus {
    private StatusAgendamento statusAnterior;
    private StatusAgendamento novoStatus;
    private LocalDateTime dataHoraRegistro;

    public MudancaDeStatus() {
        this.dataHoraRegistro = LocalDateTime.now();
    }

    public StatusAgendamento getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusAgendamento statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusAgendamento getNovoStatus() {
        return novoStatus;
    }

    public void setNovoStatus(StatusAgendamento novoStatus) {
        this.novoStatus = novoStatus;
    }

    public LocalDateTime getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(LocalDateTime dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }
}


