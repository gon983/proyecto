package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.UserStateEntity;

public interface UserStateRepository extends JpaRepository<UserStateEntity,String> {

}
