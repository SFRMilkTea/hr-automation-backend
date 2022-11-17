package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByUsername (String username);
    UserEntity findByEmail (String email);
    Page<UserEntity> findAll(Pageable pageable);
}
