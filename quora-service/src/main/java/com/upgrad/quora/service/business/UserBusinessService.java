package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service

public class UserBusinessService {

    @Autowired
    private UserDao userDao;


    @Transactional(propagation=Propagation.REQUIRED)
    public UserEntity getUser(final String userId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
       UserAuthEntity userAuthExists= userDao.getUserAuthToken(authorization);
        UserEntity getUser= userDao.getUserByUuid(userId);

       if(userAuthExists.getAccessToken()==null){
           throw new AuthorizationFailedException("ATHR-001","User has not signed in");


       }else if(userAuthExists.getLogoutAt()!=null){
           throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");

       }else{

          if(getUser==null){
              throw new UserNotFoundException("'USR-001","User with entered uuid does not exist");

          }

       }

       return getUser;
    }

}
