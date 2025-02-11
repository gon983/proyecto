package com.proyect.mvp.domain.repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.proyect.mvp.domain.model.entities.CountryEntity;

public interface CountryRepository extends R2dbcRepository<CountryEntity,String> {

 }  


