package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AdminBusinessService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBusinessService userBusinessService;

    @Transactional(propagation=Propagation.REQUIRED)
    public String getUserAuth(final String userUuid,final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserEntity getUserEntity=userBusinessService.getUser(userUuid,authorization);

        //UserAuthEntity userAuthExists= userDao.getUserAuthToken(authorization);
        //UserEntity getUser= userDao.getUserByUuid(userUuid);


        //String role=getUserEntity.getRole();
        //String role1=role;

        if(getUserEntity.getRole().equals("nonadmin")){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }

        String uuidUser=userDao.deleteUser(getUserEntity);

        return uuidUser;


    }


}
