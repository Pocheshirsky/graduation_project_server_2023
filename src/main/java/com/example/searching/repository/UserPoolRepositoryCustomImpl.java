package com.example.searching.repository;

import com.example.user.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
class UserPoolRepositoryCustomImpl implements UserPoolRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserInfo> findUserInfoByPredicate(UserInfo info) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserInfo> cq = cb.createQuery(UserInfo.class);
        Root<UserInfo> root = cq.from(UserInfo.class);
        List<Predicate> pc = new ArrayList<>();

        pc.add(cb.notEqual(root.get("uuid"), info.getUuid()));

        pc.add(cb.equal(root.get("searchTarget"), info.getSearchTarget()));

        if (info.getSearchTarget() == "relationships") {
            pc.add(cb.equal(root.get("locality"), info.getLocality()));
            pc.add(cb.notEqual(root.get("gender"), info.getGender()));
            agePredicate(info, pc, cb, root);
            pc.add(cb.equal(root.get("familyView"), info.getFamilyView()));

            if (info.getReligionImportance() == "important") {
                pc.add(cb.equal(root.get("religionImportance"), "important"));
                pc.add(cb.equal(root.get("religion"), info.getReligion()));
            } else pc.add(cb.equal(root.get("religionImportance"), "not_important"));

            //Вариантов может прийти много, надо выделять 5 наиболее подходящих
            for (int i = 0; i < info.getCharacterAccentuations().size(); i++) {

            }
        } else if (info.getSearchTarget() == "friendship") {
            pc.add(cb.equal(root.get("locality"), info.getLocality()));
            agePredicate(info, pc, cb, root);

            if (info.getReligionImportance() == "important") {
                pc.add(cb.equal(root.get("religionImportance"), "important"));
                pc.add(cb.equal(root.get("religion"), info.getReligion()));
            } else pc.add(cb.equal(root.get("religionImportance"), "not_important"));

            //Совпадение всех возможных интересов маловероятно, нужно находить того, у кого совпадение наибольшее
            //т.е. сравнивать кандидатов между собой и находить 5 наиболее подходящих (крч думать надо)
            for (int i = 0; i < info.getInterests().size(); i++) {

            }
        } else if (info.getSearchTarget() == "communication") {
            for (int i = 0; i < info.getInterests().size(); i++) {

            }
        } else if (info.getSearchTarget() == "entertainment") {
            for (var el:  info.getInterests()) {
                System.err.println(el);

            }

        }

        Predicate[] predicateArr = pc.toArray(Predicate[]::new);
        cq.where(predicateArr);

        return em.createQuery(cq).getResultList();
    }

    private void agePredicate(UserInfo info, List<Predicate> pc, CriteriaBuilder cb, Root<UserInfo> root) {
        var age = info.getAge();
        var ageMin = age;
        var ageMax = age;
        if (age < 23) {
            switch (age) {
                case 16:
                    ageMax++;
                    break;
                case 17:
                    ageMin--;
                    break;
                case 18:
                    ageMax += 5;
                    break;
                case 19:
                    ageMin--;
                    ageMax += 5;
                    break;
                case 20:
                    ageMin -= 2;
                    ageMax += 5;
                    break;
                case 21:
                    ageMin -= 3;
                    ageMax += 5;
                    break;
                case 22:
                    ageMin -= 4;
                    ageMax += 5;
                    break;
            }
        } else if (age < 40) {
            ageMin -= 5;
            ageMax += 5;
        } else {
            ageMin -= 10;
            ageMax += 10;
        }

        pc.add(cb.between(root.get("age"), ageMin, ageMax));
    }
}
