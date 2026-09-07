package com.banco.xyz.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banco.xyz.batch.entities.InteresesEntity;

@Repository
public interface InteresesRepository extends JpaRepository<InteresesEntity, Long> {

}
