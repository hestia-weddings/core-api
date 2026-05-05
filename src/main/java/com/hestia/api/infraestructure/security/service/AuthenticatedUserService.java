package com.hestia.api.infraestructure.security.service;

import com.hestia.api.domain.accounts.entity.User;
import com.hestia.api.domain.accounts.repository.UserRepository;
import com.hestia.api.infraestructure.security.principal.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final UserRepository userRepository;

    public AuthenticatedUser loadByAuthUserId(UUID authUserId) {
        User user= userRepository.findByAuthUserId(authUserId);

        return new AuthenticatedUser(user);
    }
}
