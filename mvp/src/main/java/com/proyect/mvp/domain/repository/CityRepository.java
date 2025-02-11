package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, String> {
    // List<CityEntity> findAll();
    // Optional<CityEntity> findById(String id);
    // void deleteById(String id);
    // CityEntity save(CityEntity city);
}


