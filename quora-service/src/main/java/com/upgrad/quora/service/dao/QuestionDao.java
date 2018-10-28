package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This returns a list of questions posted by a user
     * @param userEntity
     * @return list of questions
     */
    public List<QuestionEntity> getUserQuestion(final UserEntity userEntity) {
        try {

            return entityManager.createNamedQuery("questionById", QuestionEntity.class).setParameter("userid", userEntity.getId())
                    .getResultList();


        } catch (NoResultException nre) {
            return null;
        }
    }


    /**
     * This is used to save the question the the DB
     * @param questionContent
     * @return Question Entity
     */
    public QuestionEntity saveQuestion(final QuestionEntity questionContent) {
        entityManager.persist(questionContent);
        return questionContent;
    }


    /**
     * to fetch ist of all questions
     * @return list of all questions
     */
    public List<QuestionEntity> questionAll() {
        try {
            return entityManager.createNamedQuery("questionAll", QuestionEntity.class)
                    .getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }


    /**
     * To fetch question by its id
     * @param questionId
     * @return QuestionEntity
     */
    public QuestionEntity questionById(final String questionId) {
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", questionId)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }


    /**
     * Update question in DB
     * @param questionEntity
     */
    public void updateQuestion(final QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
    }

    /**
     * Delete Question from DB
     * @param questionEntity
     */
    public void deleteQuestion(final QuestionEntity questionEntity) {
        entityManager.createQuery("delete from QuestionEntity q where q.uuid =" + "'" + questionEntity.getUuid() + "'").executeUpdate();

    }
}
