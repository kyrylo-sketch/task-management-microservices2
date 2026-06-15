package com.task.auth_service.service;


import com.task.auth_service.model.RefreshToken;
import com.task.auth_service.model.User;
import com.task.auth_service.repository.RefreshTokenRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    public RefreshToken createRefreshToken(User customer){
        log.info("Creating refresh token request: customerId={}", customer.get_id());
        RefreshToken existing = refreshTokenRepo.findByUser(customer);
        if(existing != null){
            refreshTokenRepo.delete(existing);
            log.info("Refresh token has been deleted");
        }

        RefreshToken rt = new RefreshToken();
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiryAt(LocalDateTime.now().plusDays(7));
        rt.setUser(customer);
        log.info("Created refresh token successful: customerId={}", customer.get_id());
        return refreshTokenRepo.save(rt);
    }

    public boolean isValid(RefreshToken rt) {
        log.info("Validating refresh token request: customerId={}", rt.getUser().get_id());
        return rt.getExpiryAt().isAfter(LocalDateTime.now());
    }

    public RefreshToken findByToken(String token){
        log.info("Finding refresh token request: token={}", token);
        return refreshTokenRepo.findByToken(token);
    }
}
