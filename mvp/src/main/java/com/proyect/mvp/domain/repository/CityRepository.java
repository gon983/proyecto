package com.proyect.mvp.domain.repository;




import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.CityEntity;

public interface CityRepository extends R2dbcRepository<CityEntity,String> {
    

}
