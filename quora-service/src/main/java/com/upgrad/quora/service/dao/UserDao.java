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
        String userName = userEntity.getUsername();
        String userEmail = userEntity.getEmail();


        try {
            if (entityManager.createNamedQuery("userByName", UserEntity.class).setParameter("username", userName)
                    .setParameter("email", userEmail).getSingleResult().getUsername().equals(userEntity.getUsername()))
                throw new SignUpRestrictedException("SGR-001", "Try any other username. This username has already been taken");
            else if (entityManager.createNamedQuery("userByName", UserEntity.class).setParameter("username", userName)
                    .setParameter("email", userEmail).getSingleResult().getEmail().equals(userEntity.getEmail()))
                throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");

            return null;

        } catch (NoResultException nre) {
            entityManager.persist(userEntity);
            return userEntity;
        }
    }


    public UserEntity getUserByEmail(final String userName) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", userName)
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

        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class)
                    .setParameter("accessToken", accessToken).getSingleResult();
            // entityManager.merge(userAuthEntity);

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

















































