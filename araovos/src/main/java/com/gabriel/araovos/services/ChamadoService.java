package com.gabriel.araovos.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.araovos.domain.Chamado;
import com.gabriel.araovos.domain.Cliente;
import com.gabriel.araovos.domain.Tecnico;
import com.gabriel.araovos.domain.enums.Prioridade;
import com.gabriel.araovos.domain.enums.Status;
import com.gabriel.araovos.dtos.ChamadoDTO;
import com.gabriel.araovos.repositories.ChamadoRepository;
import com.gabriel.araovos.services.exceptions.ObjectNotFoundException;

import javax.validation.Valid;

@Service
public class ChamadoService {
    
    @Autowired
    private ChamadoRepository repository;
    @Autowired
    private TecnicoService tecnicoService;
    @Autowired
    private ClienteService clienteService;

    public Chamado findById(Integer id) {
        Optional<Chamado> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado! ID: " + id));
    }

	public List<Chamado> findAll() {
		return repository.findAll();
	}

    public Chamado create(@Valid ChamadoDTO objDTO) {
        return repository.save(newChamado(objDTO));
    }

    public Chamado update(Integer id, @Valid ChamadoDTO objDTO) {
        objDTO.setId(id);
        Chamado oldObj = findById(id);
        oldObj = updateChamado(oldObj, objDTO);
        return repository.save(oldObj);
    }

    private Chamado updateChamado(Chamado oldObj, ChamadoDTO obj) {
        Tecnico tecnico = tecnicoService.findById(obj.getTecnico());
        Cliente cliente = clienteService.findById(obj.getCliente());
        
        oldObj.setTecnico(tecnico);
        oldObj.setCliente(cliente);
        oldObj.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
        oldObj.setStatus(Status.toEnum(obj.getStatus()));
        oldObj.setTitulo(obj.getTitulo());
        oldObj.setObservacoes(obj.getObservacoes());
        
        if (obj.getStatus() == 0 || obj.getStatus() == 1) {
            oldObj.setDataFechamento(null);
        } else if (obj.getStatus() == 2) {
            oldObj.setDataFechamento(LocalDate.now());
        } else {
            System.out.println("Status inválido: " + obj.getStatus());
        }
        oldObj.setDataAbertura(obj.getDataAbertura());
        
        return oldObj;
    }     

    private Chamado newChamado(ChamadoDTO obj) {
        Tecnico tecnico = tecnicoService.findById(obj.getTecnico());
        Cliente cliente = clienteService.findById(obj.getCliente());
        
        Chamado chamado = new Chamado();
        if(obj.getId() != null) {
            chamado.setId(obj.getId());
        }

        if(obj.getStatus().equals(2)) {
            chamado.setDataFechamento(LocalDate.now());
        }

        chamado.setTecnico(tecnico);
        chamado.setCliente(cliente);
        chamado.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
        chamado.setStatus(Status.toEnum(obj.getStatus()));
        chamado.setTitulo(obj.getTitulo());
        chamado.setObservacoes(obj.getObservacoes());
        return chamado;
    }

}
