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
//This method first checks whether the token entered is correc, if not exception is thrown. If it is correct, it then checks whether the user is admin or not

    /**
     * This retuen the uuid if the user is valid after deleting that user
     * @param userUuid
     * @param authorization
     * @return UUID Of User
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public String getUserAuth(final String userUuid,final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserEntity getUserEntity=userBusinessService.getUser(userUuid,authorization);



        if(getUserEntity.getRole().equals("nonadmin")){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }

        String uuidUser=userDao.deleteUser(getUserEntity);

        return uuidUser;


    }


}
