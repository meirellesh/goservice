package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.domain.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query(value = "SELECT status_agendamento, COUNT(*) AS quantidade_servicos" +
            "FROM agendamentos" +
            "GROUP BY status_agendamento;", nativeQuery = true)
    void agendamentoByStatus();

    @Query(value = "SELECT a.* FROM agendamentos a JOIN usuarios u ON a.cliente_id = u.id WHERE u.email = ?1 ORDER BY data LIMIT ?2, 10", nativeQuery = true)
    List<Agendamento> findByClienteEmail(String email, int offset);

    @Query(value = "SELECT a.* FROM agendamentos a JOIN usuarios u ON a.prestador_id = u.id WHERE u.email = ?1 ORDER BY data LIMIT ?2, 10", nativeQuery = true)
    List<Agendamento> findByPrestadorEmail(String email, int offset);

    @Query(value = "SELECT a.* FROM agendamentos a WHERE a.prestador_id = ? AND a.data = ?", nativeQuery = true)
    List<Agendamento> findByPrestadorAndData(Long prestadorId, LocalDate data);

    @Query(value = "SELECT * FROM agendamentos a WHERE data BETWEEN ? AND ?", nativeQuery = true)
    List<Agendamento> findByData(String dataInicio, String dataFim);
}
