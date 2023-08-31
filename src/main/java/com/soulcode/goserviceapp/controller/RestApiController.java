package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Servico;
import com.soulcode.goserviceapp.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> 4fd336c9acba17650f0de7db8936f954fa0495f5

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class RestApiController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping(value = "/servicos")
    public List<Servico> servicos() {
<<<<<<< HEAD
        List<Servico> servicos = servicoService.findAll();
        return servicos;
    }
}
=======
        return servicoService.findAll();
    }

//    @PostMapping
//    @PutMapping
//    @DeleteMapping
}
>>>>>>> 4fd336c9acba17650f0de7db8936f954fa0495f5
