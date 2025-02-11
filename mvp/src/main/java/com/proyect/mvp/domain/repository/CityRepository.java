package com.proyect.mvp.domain.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.CityEntity;

public interface CityRepository extends JpaRepository<CityEntity,String> {
    

}
