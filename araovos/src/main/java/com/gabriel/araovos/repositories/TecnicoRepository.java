package com.gabriel.araovos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.araovos.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    
}
