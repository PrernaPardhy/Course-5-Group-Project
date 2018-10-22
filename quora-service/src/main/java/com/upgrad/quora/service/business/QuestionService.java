package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)

    public UserAuthEntity authenticate(final String authorization) throws AuthorizationFailedException {

        UserAuthEntity userTokenExists = userDao.getUserAuthToken(authorization);
        if (userTokenExists == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userTokenExists.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        } else {
            return userTokenExists;
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity saveUserQuestion(final QuestionEntity questionEntity) {
        questionDao.saveQuestion(questionEntity);
        return questionEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getQuestions() {

        return questionDao.questionAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getQuestionAuthenticate(final UserAuthEntity userAuthEntity, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.questionById(questionId);
        if(questionEntity==null){
            throw new InvalidQuestionException("QUES-001","Entered Question uuid does not exist");
        }else if (questionEntity.getUser().getUuid() != userAuthEntity.getUser().getUuid() && questionEntity.getUser().getRole() == "nonadmin") {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        return questionEntity;
    }


    @Transactional(propagation=Propagation.REQUIRED)
    public void updateUserQuestion(final QuestionEntity questionEntityt) {
      questionDao.updateQuestion(questionEntityt);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public void deleteQuestion(final QuestionEntity questionEntity){
        questionDao.deleteQuestion(questionEntity);
    }

    @Transactional(propagation=Propagation.REQUIRED)

    public List<QuestionEntity> getUserQuestion(final String userId) throws UserNotFoundException {
        UserEntity userEntity= userDao.getUserByUuid(userId);
        if(userEntity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }
            return  questionDao.getUserQuestion(userEntity);

    }
}




