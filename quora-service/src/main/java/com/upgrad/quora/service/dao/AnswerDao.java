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

    public AnswerEntity answerById(final String answerId) {
        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid", answerId)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public AnswerEntity editAnswer(final AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }
    public void deleteAnswer(final AnswerEntity answerEntity) {
        entityManager.createQuery("delete from AnswerEntity a where a.uuid =" + "'" + answerEntity.getUuid() + "'").executeUpdate();

    }

    public List<AnswerEntity> getAnswerQuestion(String questionId) {
        try {

            return entityManager.createNamedQuery("answerByQuestionID", AnswerEntity.class).setParameter("questionid", questionId)
                    .getResultList();
            // String Query=" select distinct q from QuestionEntity q inner join q.user u where u.id="+userEntity.getId();
            // return entityManager.createQuery(Query).getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }
}



