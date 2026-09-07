package com.banco.xyz.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banco.xyz.batch.entities.EstadoCuentaEntity;

@Repository
public interface EstadoCuentaRepository extends JpaRepository<EstadoCuentaEntity, Long> {

}
