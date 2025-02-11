package com.proyect.mvp.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.proyect.mvp.domain.model.entities.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity,String> {

 }  


