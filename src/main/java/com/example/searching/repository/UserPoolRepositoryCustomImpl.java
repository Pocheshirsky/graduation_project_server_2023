package com.example.searching.repository;

import com.example.user.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
class UserPoolRepositoryCustomImpl implements UserPoolRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public List<UserInfo> findUserInfoByPredicate(UserInfo info) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserInfo> cq = cb.createQuery(UserInfo.class);
        Root<UserInfo> root = cq.from(UserInfo.class);

        List<Predicate> predicates = new ArrayList<>();
//        predicates.add(cb.notEqual(root.get("uuid"),info.getUuid()));


        switch (info.getSearchTarget()) {
            case "love":
                predicates.add(cb.equal(root.get("searchTarget"), "friend"));
                break;
            case "friend":
                predicates.add(cb.equal(root.get("searchTarget"), "love"));
                break;


        }


//        predicates.add( cb.between(root.get("age"), 0,9));


        Predicate[] predicateArr = predicates.toArray(Predicate[]::new);

        cq.where(predicateArr);


        return em.createQuery(cq).getResultList();
    }
}
