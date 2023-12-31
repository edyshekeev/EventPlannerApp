package com.example.eventplannerapp.io;

import com.example.eventplannerapp.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
    Page<UserEntity> findAll(Pageable pageable);
    UserEntity findUserByEmailVerificationToken(String token);
    UserEntity findByUsername(String username);
}
