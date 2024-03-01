package com.gabriel.araovos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.araovos.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {
    
}
