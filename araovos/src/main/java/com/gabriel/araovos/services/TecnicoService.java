package com.gabriel.araovos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabriel.araovos.domain.Pessoa;
import com.gabriel.araovos.domain.Tecnico;
import com.gabriel.araovos.dtos.TecnicoDTO;
import com.gabriel.araovos.repositories.PessoaRepository;
import com.gabriel.araovos.repositories.TecnicoRepository;
import com.gabriel.araovos.services.exceptions.DataIntegrityViolationException;
import com.gabriel.araovos.services.exceptions.ObjectNotFoundException;

import javax.validation.Valid;

@Service
public class TecnicoService {
    
    @Autowired
    private TecnicoRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
	private BCryptPasswordEncoder encoder;

    public Tecnico findById(Integer id) {
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado! Id: " + id));
    }

    public List<Tecnico> findAll() {
       return repository.findAll();
    }

    public Tecnico create(TecnicoDTO objDTO) {
        objDTO.setId(null);
        objDTO.setSenha(encoder.encode(objDTO.getSenha()));
        validaPorCpfEEmail(objDTO);
        Tecnico newObj = new Tecnico(objDTO);
        return repository.save(newObj);
    }

    public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
        objDTO.setId(id);
        Tecnico oldObj = findById(id);
        validaPorCpfEEmail(objDTO);
        oldObj = new Tecnico(objDTO);
        return repository.save(oldObj);
    }

    public void delete(Integer id) {
        Tecnico obj = findById(id);
        if(obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Tecnico possui ordens de servico e nao pode ser deletado!");
        }
        repository.deleteById(id);
    }
 
    private void validaPorCpfEEmail(TecnicoDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("CPF ja cadatrado no sistema!");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("E-mail ja cadatrado no sistema!");
        }
    }

}
