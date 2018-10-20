package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation=Propagation.REQUIRED)

    public UserAuthEntity authenticate(final String authorization) throws AuthorizationFailedException {

        UserAuthEntity userTokenExists= userDao.getUserAuthToken(authorization);
        if(userTokenExists==null){
           throw new AuthorizationFailedException("ATHR-001","User has not signed in");

        }else if(userTokenExists.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }else{
           return userTokenExists;
        }

    }

    @Transactional(propagation=Propagation.REQUIRED)
    public QuestionEntity saveUserQuestion(final QuestionEntity questionEntity){
        userDao.saveQuestion(questionEntity);
        return questionEntity;

    }
}
