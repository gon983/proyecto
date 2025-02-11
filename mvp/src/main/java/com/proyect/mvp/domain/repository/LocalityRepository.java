package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.LocalityEntity;

public interface LocalityRepository extends JpaRepository<LocalityEntity,String> {

}
