package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
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

import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;


    //This method is used to validate the questionID

    /**
     * This is used to validate the questionid
     * @param questionId
     * @return QuestionEntity
     * @throws InvalidQuestionException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public QuestionEntity authenticateQuestion(final String questionId) throws InvalidQuestionException {
        QuestionEntity questionEntity= questionDao.questionById(questionId);
        if(questionEntity==null){
            throw  new InvalidQuestionException("QUES-001","The question entered is invalid");
        }else{
            return questionEntity;
        }

    }



    /**
     * //This method is used to validate userID and then check whether user is signed in or not
     * @param authorization
     * @return USER Auth Entity
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public UserAuthEntity authenticate(final String authorization) throws AuthorizationFailedException {
        UserAuthEntity userTokenExists = userDao.getUserAuthToken(authorization);
        if (userTokenExists == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userTokenExists.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a Answer");
        } else {
            return userTokenExists;
        }
    }



    /**
     * //This method is used to validate userID and then check whether user is signed in or not
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public UserAuthEntity authenticateEdit(final String authorization) throws AuthorizationFailedException {
        UserAuthEntity userTokenExists = userDao.getUserAuthToken(authorization);
        if (userTokenExists == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userTokenExists.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to Edit a Answer");
        } else {
            return userTokenExists;
        }
    }



    /**
     * //This method is used to validate userID and then check whether user is signed in or not
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public UserAuthEntity authenticateDelete(final String authorization) throws AuthorizationFailedException {
        UserAuthEntity userTokenExists = userDao.getUserAuthToken(authorization);
        if (userTokenExists == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userTokenExists.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to Delete a Answer");
        } else {
            return userTokenExists;
        }
    }



    /**
     * //This method allows the signed user to write the answer for the specified question
     * @param answerEntity
     * @return Answer Entity
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public AnswerEntity createAnswer(final AnswerEntity answerEntity){
        answerDao.saveAnswer(answerEntity);
        return  answerEntity;
    }



    /**
     * //This method is used to validate the user
     * @param userAuth
     * @param answerId
     * @return AnswerEntity
     * @throws AnswerNotFoundException
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public AnswerEntity authenticateUser(final UserAuthEntity userAuth, final String answerId) throws AnswerNotFoundException, AuthorizationFailedException {
        AnswerEntity answerEntity= answerDao.getUserById(answerId);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }else if(userAuth.getUser().getUuid()!=answerEntity.getUser().getUuid()){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");

        }else{
            return answerEntity;
        }

    }



    /**
     * //This method allows valid user to delete the answer
     * @param userAuth
     * @param answerId
     * @return AnswerEntity
     * @throws AnswerNotFoundException
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public AnswerEntity authenticateUserDelete(final UserAuthEntity userAuth, final String answerId) throws AnswerNotFoundException, AuthorizationFailedException {
        AnswerEntity answerEntity= answerDao.getUserById(answerId);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }else if(userAuth.getUser().getUuid()!=answerEntity.getUser().getUuid() && answerEntity.getUser().getRole()=="nonadmin"){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can delete the answer");

        }else{
            return answerEntity;
        }

    }


    /**
     * //This method allows the valid user to edit the answer
     * @param answerEntity
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public void editAnswer(final AnswerEntity answerEntity){
        answerDao.updateAnswer(answerEntity);
    }



    /**
     * //This method allows a valid user to delete an answer
     * @param answerEntity
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public void deleteAnswer(final AnswerEntity answerEntity){
        answerDao.deleteAns(answerEntity);
    }



    /**
     * //This method fetches all answer for a specific question
     * @param questionId
     * @return list of answers to a particular quesion
     * @throws InvalidQuestionException
     */
    @Transactional (propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAnswers(final String questionId) throws InvalidQuestionException {
        QuestionEntity questionEntity=questionDao.questionById(questionId);
        if(questionEntity==null){
            throw new InvalidQuestionException("QUES-001","The question with entered uuid whose details are to be seen does not exist");
        }
        return answerDao.ansByQuestionId(questionEntity);
    }




}
