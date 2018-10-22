package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserDao userDao;

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
        userDao.saveQuestion(questionEntity);
        return questionEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getQuestions(final String UserUuid) {

        return userDao.questionUuid(UserUuid);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getQuestionAuthenticate(final UserAuthEntity userAuthEntity, final int questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = userDao.questionById(questionId);
        if(questionEntity==null){
            throw new InvalidQuestionException("QUES-001","Entered Question uuid does not exist");
        }else if (questionEntity.getUser().getUuid() != userAuthEntity.getUser().getUuid() && questionEntity.getUser().getRole() == "nonadmin") {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        } else if (questionEntity.getUuid() == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");

        }
        return questionEntity;
    }


    @Transactional(propagation=Propagation.REQUIRED)
    public void updateUserQuestion(final QuestionEntity questionEntityt) {
      userDao.updateQuestion(questionEntityt);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public void deleteQuestion(final QuestionEntity questionEntity){
        userDao.deleteQuestion(questionEntity);
    }
}




