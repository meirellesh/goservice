package com.soulcode.goserviceapp.domain;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "logs_de_agendamento")
public class AppointmentLog {
    @Id
    private String id;
    private Long idDoAgendamento;
    private List<MudancaDeStatus> mudancasDeStatus;
    private LocalDateTime dataHoraRegistro;

    public AppointmentLog() {
        this.dataHoraRegistro = LocalDateTime.now();
    }

    public AppointmentLog(Long idDoAgendamento, List<MudancaDeStatus> mudancasDeStatus) {
        this.idDoAgendamento = idDoAgendamento;
        this.mudancasDeStatus = mudancasDeStatus;
        this.dataHoraRegistro = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdDoAgendamento() {
        return idDoAgendamento;
    }

    public void setIdDoAgendamento(Long idDoAgendamento) {
        this.idDoAgendamento = idDoAgendamento;
    }

    public List<MudancaDeStatus> getMudancasDeStatus() {
        return mudancasDeStatus;
    }

    public void setMudancasDeStatus(List<MudancaDeStatus> mudancasDeStatus) {
        this.mudancasDeStatus = mudancasDeStatus;
    }

    public LocalDateTime getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(LocalDateTime dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }
}
