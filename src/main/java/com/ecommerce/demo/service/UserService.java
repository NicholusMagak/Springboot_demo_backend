package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.ResponseDto;
import com.ecommerce.demo.dto.user.SigninDto;
import com.ecommerce.demo.dto.user.SigninResponseDto;
import com.ecommerce.demo.dto.user.SignupDto;
import com.ecommerce.demo.exceptions.AuthenticationFailException;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.model.AuthenticationToken;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationService authenticationService;

    // this ensures that both user and token are created in one transaction, else revert
    @Transactional

    public ResponseDto signUp(SignupDto signupDto) {
        //check if user already exists
        if (Objects.nonNull(userRepo.findByEmail(signupDto.getEmail()))) {
            //found a user
            throw new CustomException("user already present");
        }
        // hash pass

        String encryptedPass = signupDto.getPassword();

        try {
            encryptedPass = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(e.getMessage());
        }

        // result of the constructor made in the user model
        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encryptedPass);

        //save user
        userRepo.save(user);

        // create token
        final AuthenticationToken authenticationToken = new AuthenticationToken(user);

        authenticationService.saveConfirmationToken(authenticationToken);


        ResponseDto responseDto = new ResponseDto("success", "user created successfully");
        return responseDto;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SigninResponseDto signIn(SigninDto signinDto) {
        // find user by email
        User user = userRepo.findByEmail(signinDto.getEmail());

        // ensure the email exists
        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("user/pass not valid");
        }

        // hash the password
        // compare the password in DB
        try {
            if (!user.getPassword().equals(hashPassword(signinDto.getPassword()))) {
                throw new AuthenticationFailException("user/pass not valid");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // retrieve token once pass matches
        AuthenticationToken token = authenticationService.getToken(user);

        if (Objects.isNull(token)) {
            throw new CustomException("token is not found.");
        }

        // return response
        return new SigninResponseDto("success", token.getToken());
    }
}
