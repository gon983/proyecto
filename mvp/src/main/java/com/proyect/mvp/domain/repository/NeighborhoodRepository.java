package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.NeighborhoodEntity;

public interface NeighborhoodRepository extends JpaRepository<NeighborhoodEntity,String> {

}
