package com.lucasxchagas.gamedevbudgets.repository;

import com.lucasxchagas.gamedevbudgets.domain.Payment;
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
public class PaymentRepositoryWithBagRelationshipsImpl implements PaymentRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Payment> fetchBagRelationships(Optional<Payment> payment) {
        return payment.map(this::fetchBudgets);
    }

    @Override
    public Page<Payment> fetchBagRelationships(Page<Payment> payments) {
        return new PageImpl<>(fetchBagRelationships(payments.getContent()), payments.getPageable(), payments.getTotalElements());
    }

    @Override
    public List<Payment> fetchBagRelationships(List<Payment> payments) {
        return Optional.of(payments).map(this::fetchBudgets).orElse(Collections.emptyList());
    }

    Payment fetchBudgets(Payment result) {
        return entityManager
            .createQuery("select payment from Payment payment left join fetch payment.budgets where payment is :payment", Payment.class)
            .setParameter("payment", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Payment> fetchBudgets(List<Payment> payments) {
        return entityManager
            .createQuery(
                "select distinct payment from Payment payment left join fetch payment.budgets where payment in :payments",
                Payment.class
            )
            .setParameter("payments", payments)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
