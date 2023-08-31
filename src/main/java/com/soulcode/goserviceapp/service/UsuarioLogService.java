package com.soulcode.goserviceapp.service;

<<<<<<< HEAD
import com.soulcode.goserviceapp.domain.Usuario;
=======
>>>>>>> 4fd336c9acba17650f0de7db8936f954fa0495f5
import com.soulcode.goserviceapp.domain.UsuarioLog;
import com.soulcode.goserviceapp.repository.UsuarioLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioLogService {

    @Autowired
    private UsuarioLogRepository usuarioLogRepository;

    public UsuarioLog create(UsuarioLog usuarioLog){
        return usuarioLogRepository.save(usuarioLog);
    }

    public List<UsuarioLog> findAll(){
        return usuarioLogRepository.findAll();
    }
}
