package com.gabriel.araovos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.araovos.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
}
