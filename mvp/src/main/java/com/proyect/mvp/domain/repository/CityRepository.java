package com.proyect.mvp.domain.repository;




import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.CityEntity;

public interface CityRepository extends R2dbcRepository<CityEntity,String> {
    

}
