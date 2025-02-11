package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,String> {

}
