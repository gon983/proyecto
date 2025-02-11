package com.proyect.mvp.domain.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyect.mvp.domain.model.entities.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity,String> {

 }  


