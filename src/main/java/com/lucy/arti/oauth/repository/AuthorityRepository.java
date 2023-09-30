package com.lucy.arti.oauth.repository;

import com.lucy.arti.member.model.UserRole;
import com.lucy.arti.oauth.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorityRepository {
    private final EntityManager em;

    //    public Optional<Authority> findByAuthorityName(UserRole userRole) {
//        Authority authority = em.createQuery("select a from Authority a where a.authorityName = :authorityName", Authority.class).setParameter("authorityName", userRole).getSingleResult();
//        System.out.println("authority = " + authority);
//        return Optional.of(authority);
//    }
    public Optional<Authority> findByAuthorityName(UserRole userRole) {
        try {
            Authority authority = em.createQuery("select a from Authority a where a.authorityName = :authorityName", Authority.class)
                    .setParameter("authorityName", userRole)
                    .getSingleResult();
            System.out.println("authority = " + authority);
            return Optional.ofNullable(authority);
        } catch (NoResultException e) {
            // 엔티티가 없을 때 처리할 내용을 여기에 추가
            return Optional.empty();
        }    }
}
