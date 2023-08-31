package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.domain.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    @Query(value =
            "SELECT s.*" +
<<<<<<< HEAD
                    " FROM servicos s" +
                    " JOIN prestadores_servicos ps ON s.id = ps.servico_id" +
                    " JOIN usuarios u ON u.id = ps.prestador_id" +
                    " WHERE u.email = ?", nativeQuery = true)
    List<Servico> findByPrestadorEmail(String email);
}
=======
            " FROM servicos s" +
            " JOIN prestadores_servicos ps ON s.id = ps.servico_id" +
            " JOIN usuarios u ON u.id = ps.prestador_id" +
            " WHERE u.email = ?", nativeQuery = true)
    List<Servico> findByPrestadorEmail(String email);
}
>>>>>>> 4fd336c9acba17650f0de7db8936f954fa0495f5
