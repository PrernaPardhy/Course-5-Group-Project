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

    public List<QuestionEntity> getUserQuestion(final UserEntity userEntity) {
        try {

            return entityManager.createNamedQuery("questionById", QuestionEntity.class).setParameter("userid", userEntity.getId())
                    .getResultList();
            // String Query=" select distinct q from QuestionEntity q inner join q.user u where u.id="+userEntity.getId();
            // return entityManager.createQuery(Query).getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }





    public QuestionEntity saveQuestion(final QuestionEntity questionContent) {
        entityManager.persist(questionContent);
        return questionContent;
    }


    public List<QuestionEntity> questionAll() {
        try {
            return entityManager.createNamedQuery("questionAll", QuestionEntity.class)
                    .getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }




    public QuestionEntity questionById(final String questionId) {
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", questionId)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }


    public void updateQuestion(final QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
    }

    public void deleteQuestion(final QuestionEntity questionEntity) {
        entityManager.createQuery("delete from QuestionEntity q where q.uuid =" + "'" + questionEntity.getUuid() + "'").executeUpdate();

    }
}
