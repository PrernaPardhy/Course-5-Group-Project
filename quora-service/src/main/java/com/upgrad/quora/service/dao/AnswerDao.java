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

    public QuestionEntity questionAuth(final String questionId) {
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", questionId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;


        }

    }

    public AnswerEntity saveAnswer(final AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getUserById(final String answerId) {
        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid", answerId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    public void updateAnswer(final AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);

    }

    public void deleteAns(final AnswerEntity answerEntity) {
        entityManager.createQuery("delete from AnswerEntity a where a.uuid=" + "'" + answerEntity.getUuid() + "'").executeUpdate();
    }

    public List<AnswerEntity> ansByQuestionId(final QuestionEntity questionEntity) {
        try {
            return entityManager.createNamedQuery("ansByQuestion", AnswerEntity.class).setParameter("questionId", questionEntity.getId())
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}










