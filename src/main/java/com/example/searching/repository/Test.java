package com.example.searching.repository;

import com.example.searching.model.UserPool;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class Test {
    EntityManager em;

    // constructor

    List<UserPool> findUserForRelationship(
            String locality, String gender, Integer age, String familyView,
            Integer interestedGrowth, Integer growth) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserPool> cq = cb.createQuery(UserPool.class);

        Root<UserPool> user = cq.from(UserPool.class);

        Predicate localityPredicate = cb.equal(user.get("locality"), locality);
        Predicate genderPredicate = cb.notEqual(user.get("gender"), gender);
        Predicate agePredicate = cb.equal(user.get("age"), age);
        Predicate familyViewPredicate = cb.equal(user.get("familyView"), familyView);
        Predicate interestedGrowthPredicate = cb.equal(user.get("interestedGrowth"), interestedGrowth);
        Predicate growthPredicate = cb.equal(user.get("growth"), growth);

        cq.where(localityPredicate, genderPredicate, agePredicate, familyViewPredicate,
                interestedGrowthPredicate, growthPredicate);

        TypedQuery<UserPool> query = em.createQuery(cq);
        return query.getResultList();
    }

    List<UserPool> findUserForFriendship(String authorName, String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserPool> cq = cb.createQuery(UserPool.class);

        Root<UserPool> book = cq.from(UserPool.class);
        Predicate authorNamePredicate = cb.equal(book.get("author"), authorName);
        Predicate titlePredicate = cb.like(book.get("title"), "%" + title + "%");
        cq.where(authorNamePredicate, titlePredicate);

        TypedQuery<UserPool> query = em.createQuery(cq);
        return query.getResultList();
    }

    List<UserPool> findUserForCommunication(String authorName, String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserPool> cq = cb.createQuery(UserPool.class);

        Root<UserPool> book = cq.from(UserPool.class);
        Predicate authorNamePredicate = cb.equal(book.get("author"), authorName);
        Predicate titlePredicate = cb.like(book.get("title"), "%" + title + "%");
        cq.where(authorNamePredicate, titlePredicate);

        TypedQuery<UserPool> query = em.createQuery(cq);
        return query.getResultList();
    }

    List<UserPool> findUserForEntertainment(String authorName, String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserPool> cq = cb.createQuery(UserPool.class);

        Root<UserPool> book = cq.from(UserPool.class);
        Predicate authorNamePredicate = cb.equal(book.get("author"), authorName);
        Predicate titlePredicate = cb.like(book.get("title"), "%" + title + "%");
        cq.where(authorNamePredicate, titlePredicate);

        TypedQuery<UserPool> query = em.createQuery(cq);
        return query.getResultList();
    }
}
