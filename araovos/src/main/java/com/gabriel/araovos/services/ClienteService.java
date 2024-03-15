package com.gabriel.araovos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabriel.araovos.domain.Pessoa;
import com.gabriel.araovos.domain.Cliente;
import com.gabriel.araovos.dtos.ClienteDTO;
import com.gabriel.araovos.repositories.PessoaRepository;
import com.gabriel.araovos.repositories.ClienteRepository;
import com.gabriel.araovos.services.exceptions.DataIntegrityViolationException;
import com.gabriel.araovos.services.exceptions.ObjectNotFoundException;

import javax.validation.Valid;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
	private BCryptPasswordEncoder encoder;

    public Cliente findById(Integer id) {
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado! Id: " + id));
    }

    public List<Cliente> findAll() {
       return repository.findAll();
    }

    public Cliente create(ClienteDTO objDTO) {
        objDTO.setId(null);
        objDTO.setSenha(encoder.encode(objDTO.getSenha()));
        validaPorCpfEEmail(objDTO);
        Cliente newObj = new Cliente(objDTO);
        return repository.save(newObj);
    }

    public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		objDTO.setId(id);
		Cliente oldObj = findById(id);
		
		if(!objDTO.getSenha().equals(oldObj.getSenha())) 
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		
		validaPorCpfEEmail(objDTO);
		oldObj = new Cliente(objDTO);
		return repository.save(oldObj);

    public void delete(Integer id) {
        Cliente obj = findById(id);
        if(obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Cliente possui ordens de servico e nao pode ser deletado!");
        }
        repository.deleteById(id);
    }
 
    private void validaPorCpfEEmail(ClienteDTO objDTO) {
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
