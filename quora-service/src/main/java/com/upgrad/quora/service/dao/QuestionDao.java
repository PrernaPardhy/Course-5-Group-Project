package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<QuestionEntity> getUserQuestion(final String userId){
       try{
            return  entityManager.createNamedQuery("questionContents", QuestionEntity.class)
                   .setParameter("uuid", userId).getResultList();
       }catch(NoResultException nre){
           return null;
       }
    }

    public QuestionEntity saveQuestion(final QuestionEntity questionContent) {
        entityManager.persist(questionContent);
        return questionContent;
    }


    public List<QuestionEntity> questionAll() {
        List<QuestionEntity> listQuestions = entityManager.createNamedQuery("questionAll", QuestionEntity.class)
                .getResultList();
        return listQuestions;
    }

    public QuestionEntity questionById(final int questionId) {
        try {
            return entityManager.createNamedQuery("questionById", QuestionEntity.class).setParameter("id", questionId)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }


    public void updateQuestion(final QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
    }

    public void deleteQuestion(final QuestionEntity questionEntity) {
        entityManager.createQuery("delete from QuestionEntity q where q.id =" + "'" + questionEntity.getId() + "'").executeUpdate();
        //  entityManager.createNamedQuery("deleteQuestion", QuestionEntity.class).setParameter("id", questionEntity.getId());
    }
}
