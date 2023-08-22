package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.Administrador;
import com.soulcode.goserviceapp.domain.Cliente;
import com.soulcode.goserviceapp.domain.Prestador;
import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.repository.UsuarioRepository;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id){
        Optional<Usuario> result = usuarioRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new RuntimeException();
    }

    public Usuario createUser(Usuario usuario){
        String passwordEncoder = encoder.encode(usuario.getPassword());
        usuario.setSenha(passwordEncoder);
        usuario.setId(null);

        switch(usuario.getPerfil()){
            case ADMIN:
                return createAndSaveAdministrador(usuario);
            case PRESTADOR:
                return createAndSavePrestador(usuario);
            case CLIENTE:
            default:
                return createAndSaveCliente(usuario);


        }
    }

    private Administrador createAndSaveAdministrador(Usuario usuario){
        Administrador admin = new Administrador(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil(), usuario.getHabilitado());
        return usuarioRepository.save(admin);
    }

    private Prestador createAndSavePrestador(Usuario usuario){
        Prestador prestador = new Prestador(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil(), usuario.getHabilitado());
        return usuarioRepository.save(prestador);
    }

  private Cliente createAndSaveCliente(Usuario usuario){
        Cliente cliente = new Cliente(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil(), usuario.getHabilitado());
        return usuarioRepository.save(cliente);
  }



}
