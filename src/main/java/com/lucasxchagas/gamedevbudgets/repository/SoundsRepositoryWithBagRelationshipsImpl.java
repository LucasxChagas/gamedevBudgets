package com.lucasxchagas.gamedevbudgets.repository;

import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SoundsRepositoryWithBagRelationshipsImpl implements SoundsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Sounds> fetchBagRelationships(Optional<Sounds> sounds) {
        return sounds.map(this::fetchBudgets);
    }

    @Override
    public Page<Sounds> fetchBagRelationships(Page<Sounds> sounds) {
        return new PageImpl<>(fetchBagRelationships(sounds.getContent()), sounds.getPageable(), sounds.getTotalElements());
    }

    @Override
    public List<Sounds> fetchBagRelationships(List<Sounds> sounds) {
        return Optional.of(sounds).map(this::fetchBudgets).orElse(Collections.emptyList());
    }

    Sounds fetchBudgets(Sounds result) {
        return entityManager
            .createQuery("select sounds from Sounds sounds left join fetch sounds.budgets where sounds is :sounds", Sounds.class)
            .setParameter("sounds", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Sounds> fetchBudgets(List<Sounds> sounds) {
        return entityManager
            .createQuery("select distinct sounds from Sounds sounds left join fetch sounds.budgets where sounds in :sounds", Sounds.class)
            .setParameter("sounds", sounds)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
