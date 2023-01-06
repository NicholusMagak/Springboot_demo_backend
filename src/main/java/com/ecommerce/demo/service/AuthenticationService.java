package com.ecommerce.demo.service;

import com.ecommerce.demo.exceptions.AuthenticationFailException;
import com.ecommerce.demo.model.AuthenticationToken;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {
    @Autowired
    TokenRepo tokenRepo;
    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepo.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return tokenRepo.findByUser(user);
    }

    public User getUser(String token) {
        final AuthenticationToken auth_token = tokenRepo.findByToken(token);
        if (Objects.isNull(auth_token)) {
            return null;
        }
        // token is not null
        return auth_token.getUser();
    }

    public void authenticate(String token) {
        // check if token is null
        if (Objects.isNull(token)) {
            // throw an exception
            throw new AuthenticationFailException("token is not present");
        }
        if(Objects.isNull(getUser(token))) {
            throw new AuthenticationFailException("token not valid");
        }
    }
}
