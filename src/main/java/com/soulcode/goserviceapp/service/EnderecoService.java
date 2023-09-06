package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.Cliente;
import com.soulcode.goserviceapp.domain.Endereco;
import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.repository.EnderecoRepository;
import com.soulcode.goserviceapp.service.exceptions.EnderecoNaoEncontradoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;


    public Endereco findById(Long id){
        Optional<Endereco> endereco = enderecoRepository.findById(id);
        if(endereco.isPresent()){
            return endereco.get();
        } else {
            throw  new EnderecoNaoEncontradoException();
        }
    }

    public Endereco updateEndereco(Endereco endereco){
        Endereco updatedEndereco = this.findById(endereco.getId());
        updatedEndereco.setRua(endereco.getRua());
        updatedEndereco.setCidade(endereco.getCidade());
        updatedEndereco.setLogradouro(endereco.getLogradouro());
        updatedEndereco.setNumero(endereco.getNumero());
        updatedEndereco.setUf(endereco.getUf());
        return enderecoRepository.save(updatedEndereco);
    }


}