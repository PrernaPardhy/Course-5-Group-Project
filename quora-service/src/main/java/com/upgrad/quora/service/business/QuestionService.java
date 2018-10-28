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

    /**
     * This method is used to validate user by accesstoken
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
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

    /**
     * This method allows a valid user to edit a question
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    public UserAuthEntity authenticateEdit(final String authorization) throws AuthorizationFailedException {

        UserAuthEntity userTokenExists = userDao.getUserAuthToken(authorization);
        if (userTokenExists == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userTokenExists.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to Edit a question");
        } else {
            return userTokenExists;
        }

    }

    /**
     * This method allows a valid user to delete a question
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    public UserAuthEntity authenticateDelete(final String authorization) throws AuthorizationFailedException {

        UserAuthEntity userTokenExists = userDao.getUserAuthToken(authorization);
        if (userTokenExists == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userTokenExists.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to Delete a question");
        } else {
            return userTokenExists;
        }

    }

    /**
     * This method saves the new question
     * @param questionEntity
     * @return QuestionEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity saveUserQuestion(final QuestionEntity questionEntity) {
        questionDao.saveQuestion(questionEntity);
        return questionEntity;

    }

    /**
     * This method is used to disply all questions
     * @return list of all questions
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getQuestions() {

        return questionDao.questionAll();
    }

    /**
     * This method is used to validate a questionID
     * @param userAuthEntity
     * @param questionId
     * @return QuestionEntity
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getQuestionAuthenticate(final UserAuthEntity userAuthEntity, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.questionById(questionId);
        if(questionEntity==null){
            throw new InvalidQuestionException("QUES-001","Entered Question uuid does not exist");
        }else if (questionEntity.getUser().getUuid() != userAuthEntity.getUser().getUuid() && questionEntity.getUser().getRole() == "nonadmin") {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can delete the question");
        }
        return questionEntity;
    }

    /**
     * to check if the user is valid to edit a question
     * @param userAuthEntity
     * @param questionId
     * @return QuestionEntity
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getQuestionAuthenticateEdit(final UserAuthEntity userAuthEntity, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.questionById(questionId);
        if(questionEntity==null){
            throw new InvalidQuestionException("QUES-001","Entered Question uuid does not exist");
        }else if (questionEntity.getUser().getUuid() != userAuthEntity.getUser().getUuid()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        return questionEntity;
    }


    /**
     * This is to update the question
     * @param questionEntityt
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public void updateUserQuestion(final QuestionEntity questionEntityt) {
        questionDao.updateQuestion(questionEntityt);
    }

    /**
     * this is to delete the question
     * @param questionEntity
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public void deleteQuestion(final QuestionEntity questionEntity){
        questionDao.deleteQuestion(questionEntity);
    }

    /**
     * This returns the questions by a valid user
     * @param userId
     * @return list of questions
     * @throws UserNotFoundException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public List<QuestionEntity> getUserQuestion(final String userId) throws UserNotFoundException {
        UserEntity userEntity= userDao.getUserByUuid(userId);
        if(userEntity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }
        return  questionDao.getUserQuestion(userEntity);

    }
}




