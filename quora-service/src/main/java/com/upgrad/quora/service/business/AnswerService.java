package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {
    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private UserDao userDao;


   @Transactional(propagation=Propagation.REQUIRED)

    public QuestionEntity authenticateQuestion(final String questionId) throws InvalidQuestionException {
       QuestionEntity questionEntity= answerDao.questionAuth(questionId);
       if(questionEntity==null){
           throw  new InvalidQuestionException("QUES-001","The question entered is invalid");
       }else{
           return questionEntity;
       }

    }

    @Transactional(propagation=Propagation.REQUIRED)
    public UserAuthEntity anthenticate(final String authorization) throws AuthorizationFailedException {
        UserAuthEntity userTokenExists = userDao.getUserAuthToken(authorization);
        if (userTokenExists == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userTokenExists.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a Answer");
        } else {
            return userTokenExists;
        }
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public AnswerEntity createAnswer(final AnswerEntity answerEntity){
       answerDao.saveAnswer(answerEntity);
       return  answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity getAnswerAuthenticate(final UserAuthEntity userAuthEntity, final String answerId) throws AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerDao.answerById(answerId);
        if(answerEntity==null){
            throw new AnswerNotFoundException("ANS-001","Entered Answer uuid does not exist");
        }else if (answerEntity.getUser().getUuid() != userAuthEntity.getUser().getUuid() && answerEntity.getUser().getRole() == "nonadmin") {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        return answerEntity;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public AnswerEntity editAnswer(final AnswerEntity answerEntity){
        answerDao.saveAnswer(answerEntity);
        return  answerEntity;
    }


}
