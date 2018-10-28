package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(final UserEntity userEntity) throws SignUpRestrictedException {
        entityManager.persist(userEntity);
        return userEntity;
    }
    public UserEntity checkUserName(final UserEntity userEntity) throws SignUpRestrictedException {
        String userName = userEntity.getUsername();


        try {
            return entityManager.createNamedQuery("userByName",UserEntity.class).setParameter("username",userName).getSingleResult();

        } catch (NoResultException nre) {

            return null;

        }
    }

    public UserEntity checkEmail(final UserEntity userEntity) throws SignUpRestrictedException {

        String userEmail = userEntity.getEmail();

        try {
            return entityManager.createNamedQuery("userByEmail",UserEntity.class).setParameter("email",userEmail).getSingleResult();

        } catch (NoResultException nre) {

            return null;

        }
    }


    public UserEntity getUserByUserName(final String userName) {
        try {
            return entityManager.createNamedQuery("userByName", UserEntity.class).setParameter("username", userName)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity createAuthToken(UserAuthEntity userAuthEntity) {

        entityManager.persist(userAuthEntity);
        return userAuthEntity;

    }

    public UserAuthEntity getUserAuthToken(final String accessToken) {

        UserAuthEntity userAuthEntity;
        try {
            userAuthEntity = entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class)
                    .setParameter("accessToken", accessToken).getSingleResult();

            return userAuthEntity;


        } catch (NoResultException nre) {
            return null;
        }
    }

    public void updateAuthToken(final UserAuthEntity userAuthEntity) {

        entityManager.merge(userAuthEntity);
    }

    public UserEntity getUserByUuid(final String userUuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public String deleteUser(UserEntity userEntity) {
        // entityManager.createNamedQuery("deleteUser", UserEntity.class).setParameter("uuid", userEntity.getUuid());
        String userUuid = userEntity.getUuid();
        entityManager.createQuery("delete from UserEntity u where u.uuid =" + "'" + userUuid + "'").executeUpdate();
        return userEntity.getUuid();
    }


}

















































