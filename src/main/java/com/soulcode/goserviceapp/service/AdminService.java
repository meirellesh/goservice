package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.Administrador;

import com.soulcode.goserviceapp.repository.AdminRepository;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;



    public Administrador findAuthenticated(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()){
            Optional<Administrador> admin = adminRepository.findByEmail(authentication.getName());
            if(admin.isPresent()){
                return admin.get();
            } else {
                throw new UsuarioNaoEncontradoException();
            }
        } else {
            throw new UsuarioNaoAutenticadoException();
        }
    }

    public Administrador findById(Long id){
        Optional<Administrador> admin = adminRepository.findById(id);
        if(admin.isPresent()){
            return admin.get();
        } else {
            throw  new UsuarioNaoEncontradoException();
        }
    }
    public Administrador update(Administrador administrador) {
        Administrador updatedAdmin = this.findById(administrador.getId());
        updatedAdmin.setNome(administrador.getNome());
        updatedAdmin.setEmail(administrador.getEmail());
        updatedAdmin.setFotoUsuario(administrador.getFotoUsuario());
        return adminRepository.save(updatedAdmin);
    }
}
