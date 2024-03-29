package com.lucy.arti.jwt;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRepository {

    private final EntityManager em;

    public void saveRefreshToken(RefreshToken refreshToken){
        em.persist(refreshToken);
    }

    //Delete 함수 구현
    @Transactional
    public void deleteRefreshToken(RefreshToken refreshToken){
        log.info("hi");
        log.info(refreshToken.getValue());
        em.remove(refreshToken);
    }

    public Optional<RefreshToken> findByKey(String key){
        RefreshToken refreshToken = em
                .createQuery("select t from RefreshToken t where t.key = :key", RefreshToken.class)
                .setParameter("key", key)
                .getSingleResult();
        return Optional.of(refreshToken);
    }

    public boolean existsByKey(String key){
        try{
            RefreshToken refreshToken = em
                    .createQuery("select t from RefreshToken t where t.key = :key", RefreshToken.class)
                    .setParameter("key", key)
                    .getSingleResult();
            return true;
        }catch(Exception e){
            return false;
        }
    }
}