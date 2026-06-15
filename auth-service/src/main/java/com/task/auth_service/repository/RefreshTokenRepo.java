package com.task.auth_service.repository;


import com.task.auth_service.model.RefreshToken;
import com.task.auth_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepo extends MongoRepository<RefreshToken, String> {
    RefreshToken findByToken(String token);
    RefreshToken findByUser(User user);
}
