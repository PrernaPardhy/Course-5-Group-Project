package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Validates QuestionID
     * @param questionId
     * @return QuestionID
     */
    public QuestionEntity questionAuth(final String questionId) {
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", questionId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;


        }

    }

    /**
     * Persist answer to DB
     * @param answerEntity
     * @return AnswerEntity
     */
    public AnswerEntity saveAnswer(final AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /**
     * Validate AnswerID
     * @param answerId
     * @return AnswerEntity
     */
    public AnswerEntity getUserById(final String answerId) {
        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid", answerId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    /**
     * Update answer
     * @param answerEntity
     */
    public void updateAnswer(final AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);

    }

    /**
     * Delete answer from DB
     * @param answerEntity
     */
    public void deleteAns(final AnswerEntity answerEntity) {
        entityManager.createQuery("delete from AnswerEntity a where a.uuid=" + "'" + answerEntity.getUuid() + "'").executeUpdate();
    }

    /**
     * To fetch a list of all answers to a particular question
     * @param questionEntity
     * @return list of answers to a particular question
     */
    public List<AnswerEntity> ansByQuestionId(final QuestionEntity questionEntity) {
        try {
            return entityManager.createNamedQuery("ansByQuestion", AnswerEntity.class).setParameter("questionId", questionEntity.getId())
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}










