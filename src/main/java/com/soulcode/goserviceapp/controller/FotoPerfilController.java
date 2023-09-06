package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class FotoPerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute
    public void buscaUsuario(Authentication authentication, Model model){
        try {
            Usuario usuarioLogado =  usuarioService.findByEmail(authentication.getName());
            System.err.println(usuarioLogado);
            model.addAttribute("usuarioLogado", usuarioLogado);
        }catch (Exception ex){
            System.err.println("Erro ao encontrar usuario");
        }
    }
}