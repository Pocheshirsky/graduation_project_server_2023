package com.example.searching.repository;

import com.example.searching.model.UserPool;
import com.example.user.model.UserInfo;
import com.example.user.model.UserInfo_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
class UserPoolRepositoryCustomImpl implements UserPoolRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
//        var alreadyFoundUsers = poolMessageRepository.findPoolMessageByUserUuid(info.getUuid());
//        alreadyFoundUsers.forEach(poolMessage -> pc.add(cb.notEqual(root.get(UserInfo_.UUID), poolMessage.getFoundUserInfo().getUuid())));

    @Autowired
    private PoolMessageRepository poolMessageRepository;

    @Override
    public List<UserPool> findUserInfoByPredicate(UserInfo info) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserPool> cq = cb.createQuery(UserPool.class);
        Root<UserPool> rt = cq.from(UserPool.class);
        Join<UserPool, UserInfo> root = rt.join("userInfo", JoinType.LEFT);
        List<Predicate> pc = new ArrayList<>();

        if (info.getUuid() != null)
            pc.add(cb.notEqual(root.get(UserInfo_.UUID), info.getUuid()));

        if (info.getSearchTarget() == null)
            pc.add(cb.isNull(root.get(UserInfo_.SEARCH_TARGET)));
        else {
            pc.add(cb.equal(root.get(UserInfo_.SEARCH_TARGET), info.getSearchTarget()));

            if (info.getSearchTarget().equals("relationships")) {
                if (info.getLocality() != null)
                    pc.add(cb.equal(root.get(UserInfo_.LOCALITY), info.getLocality()));
                else pc.add(cb.isNull(root.get(UserInfo_.LOCALITY)));

                if (info.getGender() != null)
                    pc.add(cb.notEqual(root.get(UserInfo_.GENDER), info.getGender()));
                else pc.add(cb.isNull(root.get(UserInfo_.GENDER)));

                if (info.getAge() != 0)
                    agePredicate(info, pc, cb, root);
                else pc.add(cb.equal(root.get(UserInfo_.AGE), 0));

                if (info.getFamilyView() != null)
                    pc.add(cb.equal(root.get(UserInfo_.FAMILY_VIEW), info.getFamilyView()));
                else pc.add(cb.isNull(root.get(UserInfo_.FAMILY_VIEW)));

                if (info.getReligionImportance() != null) {
                    if (info.getReligionImportance().equals("important")) {
                        pc.add(cb.equal(root.get(UserInfo_.RELIGION_IMPORTANCE), "important"));
                        if (info.getReligion() != null)
                            pc.add(cb.equal(root.get(UserInfo_.RELIGION), info.getReligion()));
                        else pc.add(cb.isNull(root.get(UserInfo_.RELIGION)));
                    } else pc.add(cb.equal(root.get(UserInfo_.RELIGION_IMPORTANCE), "not_important"));
                } else pc.add(cb.isNull(root.get(UserInfo_.RELIGION_IMPORTANCE)));

                if (info.getCharacterAccentuations() == null && info.getInterestedCharacterAccentuations() == null) {
                    pc.add(cb.isNull(root.get(UserInfo_.CHARACTER_ACCENTUATIONS)));
                } else if (info.getInterestedCharacterAccentuations() == null)
                    pc.add(cb.isNotNull(root.get(UserInfo_.CHARACTER_ACCENTUATIONS)));

            } else if (info.getSearchTarget().equals("friendship")) {

                if (info.getLocality() != null)
                    pc.add(cb.equal(root.get(UserInfo_.LOCALITY), info.getLocality()));
                else pc.add(cb.isNull(root.get(UserInfo_.LOCALITY)));

                if (info.getAge() != 0)
                    agePredicate(info, pc, cb, root);
                else pc.add(cb.equal(root.get(UserInfo_.AGE), 0));

                if (info.getReligionImportance() != null) {
                    if (info.getReligionImportance().equals("religionImportance")) {
                        pc.add(cb.equal(root.get(UserInfo_.RELIGION_IMPORTANCE), "important"));
                        if (info.getReligion() != null)
                            pc.add(cb.equal(root.get(UserInfo_.RELIGION), info.getReligion()));
                        else pc.add(cb.isNull(root.get(UserInfo_.RELIGION)));
                    } else pc.add(cb.equal(root.get(UserInfo_.RELIGION_IMPORTANCE), "not_important"));
                } else pc.add(cb.isNull(root.get(UserInfo_.RELIGION_IMPORTANCE)));
            }
        }

        Predicate[] predicateArr = pc.toArray(Predicate[]::new);
        cq.where(predicateArr);

        var result = em.createQuery(cq).getResultList();

        List<UserPool> acceptableUsers = new ArrayList<>();


        if(info.getSearchTarget() != null) {
            if (info.getSearchTarget().equals("relationships")) {
                var currentUserAccentuations = info.getCharacterAccentuations();
                var currentUserInterestedAccentuations = info.getInterestedCharacterAccentuations();
                nap:
                for (var user : result) {
                    for (int i = 0; i < user.getUserInfo().getCharacterAccentuations().size(); i++) {
                        if (Integer.parseInt(currentUserAccentuations.get(i)) <
                                Integer.parseInt(user.getUserInfo().getInterestedCharacterAccentuations().get(i)) &&
                                Integer.parseInt(currentUserInterestedAccentuations.get(i)) >
                                        Integer.parseInt(user.getUserInfo().getCharacterAccentuations().get(i)))
                            continue nap;
                    }
                    acceptableUsers.add(user);
                }
                return acceptableUsers;
            }
            else if(info.getSearchTarget().equals("entertainment") ||
                    info.getSearchTarget().equals("friendship") ||
                    info.getSearchTarget().equals("communication")) {
                var interests = info.getInterests();
                nam:
                for (var user : result) {
                    for (int i = 0; i < interests.size(); i++) {
                        for (int j = 0; j < user.getUserInfo().getInterests().size(); j++) {
                            if (interests.get(i).equals(user.getUserInfo().getInterests().get(j))) {
                                acceptableUsers.add(user);
                                continue nam;
                            }
                        }
                    }
                }
            }
            return acceptableUsers;
        }
        else
            return result;
    }

    private void agePredicate(UserInfo info, List<Predicate> pc, CriteriaBuilder cb, Join<UserPool, UserInfo> root) {
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
