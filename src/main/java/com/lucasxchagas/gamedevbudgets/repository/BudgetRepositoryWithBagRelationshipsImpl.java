package com.lucasxchagas.gamedevbudgets.repository;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
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
public class BudgetRepositoryWithBagRelationshipsImpl implements BudgetRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Budget> fetchBagRelationships(Optional<Budget> budget) {
        return budget.map(this::fetchSounds).map(this::fetchPayments);
    }

    @Override
    public Page<Budget> fetchBagRelationships(Page<Budget> budgets) {
        return new PageImpl<>(fetchBagRelationships(budgets.getContent()), budgets.getPageable(), budgets.getTotalElements());
    }

    @Override
    public List<Budget> fetchBagRelationships(List<Budget> budgets) {
        return Optional.of(budgets).map(this::fetchSounds).map(this::fetchPayments).orElse(Collections.emptyList());
    }

    Budget fetchSounds(Budget result) {
        return entityManager
            .createQuery("select budget from Budget budget left join fetch budget.sounds where budget is :budget", Budget.class)
            .setParameter("budget", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Budget> fetchSounds(List<Budget> budgets) {
        return entityManager
            .createQuery("select distinct budget from Budget budget left join fetch budget.sounds where budget in :budgets", Budget.class)
            .setParameter("budgets", budgets)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Budget fetchPayments(Budget result) {
        return entityManager
            .createQuery("select budget from Budget budget left join fetch budget.payments where budget is :budget", Budget.class)
            .setParameter("budget", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Budget> fetchPayments(List<Budget> budgets) {
        return entityManager
            .createQuery("select distinct budget from Budget budget left join fetch budget.payments where budget in :budgets", Budget.class)
            .setParameter("budgets", budgets)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
