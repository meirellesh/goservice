package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.*;
import com.soulcode.goserviceapp.domain.enums.StatusAgendamento;
import com.soulcode.goserviceapp.repository.AgendamentoRepository;
import com.soulcode.goserviceapp.repository.AppointmentLogRepository;
import com.soulcode.goserviceapp.service.exceptions.AgendamentoNaoEncontradoException;
import com.soulcode.goserviceapp.service.exceptions.StatusAgendamentoImutavelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private  ServicoService servicoService;

    @Autowired
    private AppointmentLogRepository appointmentLogRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PrestadorService prestadorService;

    public Agendamento findById(Long id){
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        if(agendamento.isPresent()) {
            return agendamento.get();
        }
        throw new AgendamentoNaoEncontradoException();
    }

    public Agendamento create(Authentication authentication, Long servicoId, Long prestadorId, LocalDate data, LocalTime hora){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Prestador prestador = prestadorService.findById(prestadorId);
        Servico servico = servicoService.findById(servicoId);
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setPrestador(prestador);
        agendamento.setServico(servico);
        agendamento.setData(data);
        agendamento.setHora(hora);

        return agendamentoRepository.save(agendamento);
    }
    @Cacheable(cacheNames = "redisCache")
    public List<Agendamento> findByCliente(Authentication authentication, int page){
        System.err.println("BUSCANDO AGENDAMENTOS CLIENTE NO BANCO...");
        Cliente cliente = clienteService.findAuthenticated(authentication);
        int offset = page * 10;
        return agendamentoRepository.findByClienteEmail(cliente.getEmail(), offset);
    }

    @Cacheable(cacheNames = "redisCache")
    public List<Agendamento> findByPrestador(Authentication authentication, int page){
        System.err.println("BUSCANDO AGENDAMENTOS PRESTADOR NO BANCO...");
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        int offset = page * 10;
        return  agendamentoRepository.findByPrestadorEmail(prestador.getEmail(), offset);
    }

    public Page<Agendamento> findAgendamentoByPage(Pageable pageable) {
        return agendamentoRepository.findAll(pageable);
    }

    public void cancelAgendaPrestador(Authentication authentication, Long id){
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if(agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_PRESTADOR);
            agendamentoRepository.save(agendamento);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void confirmAgenda(Authentication authentication, Long id){
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if(agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CONFIRMADO);
            agendamentoRepository.save(agendamento);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void cancelAgendaCliente(Authentication authentication, Long id){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_CLIENTE);
            agendamentoRepository.save(agendamento);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void completeAgenda(Authentication authentication, Long id){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.CONFIRMADO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CONCLUIDO);
            agendamentoRepository.save(agendamento);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void cancelarAgendamentoPeloPrestador(Authentication authentication, Long id) {
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)) {
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_PRESTADOR);
            agendamentoRepository.save(agendamento);

            criarEntradaDeMudancaDeStatus(agendamento.getId(), StatusAgendamento.AGUARDANDO_CONFIRMACAO, StatusAgendamento.CANCELADO_PELO_PRESTADOR);

            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    private void criarEntradaDeMudancaDeStatus(Long idDoAgendamento, StatusAgendamento statusAnterior, StatusAgendamento novoStatus) {
        AppointmentLog logDeAgendamento = new AppointmentLog();
        logDeAgendamento.setIdDoAgendamento(idDoAgendamento);
        logDeAgendamento.setDataHoraRegistro(LocalDateTime.now());

        MudancaDeStatus mudancaDeStatus = new MudancaDeStatus();
        mudancaDeStatus.setStatusAnterior(statusAnterior);
        mudancaDeStatus.setNovoStatus(novoStatus);
        mudancaDeStatus.setDataHoraRegistro(LocalDateTime.now());

        logDeAgendamento.setMudancasDeStatus(Collections.singletonList(mudancaDeStatus));

        appointmentLogRepository.save(logDeAgendamento);
    }
}
