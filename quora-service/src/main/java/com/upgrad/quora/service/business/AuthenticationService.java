package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation=Propagation.REQUIRED)
    public UserAuthEntity authenticate(final String username, final String password) throws AuthenticationFailedException{

       UserEntity userEntity= userDao.getUserByEmail(username);

       if(userEntity==null){
           throw new AuthenticationFailedException("ATH-001","Username does not exists");
       }

       final String encryptedPass=cryptographyProvider.encrypt(password,userEntity.getSalt());

       if(encryptedPass.equals(userEntity.getPassword())){
           JwtTokenProvider jwtTokenProvider=new JwtTokenProvider(encryptedPass);

           UserAuthEntity userAuthToken=new UserAuthEntity();
           userAuthToken.setUser(userEntity);
           userAuthToken.setUuid(userEntity.getUuid());
           ZonedDateTime now=ZonedDateTime.now();
           ZonedDateTime expiresAt=now.plusHours(8);

           userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(),now,expiresAt));
           userAuthToken.setLoginAt(now);
           userAuthToken.setExpiresAt(expiresAt);
           userAuthToken.setLogoutAt(now);

           userDao.createAuthToken(userAuthToken);

           return userAuthToken;

       }else{
           throw new AuthenticationFailedException("ATH-002","Password failed");
       }

    }
}
