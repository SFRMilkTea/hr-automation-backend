package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername (String username);
    UserEntity findByEmail (String email);
}
