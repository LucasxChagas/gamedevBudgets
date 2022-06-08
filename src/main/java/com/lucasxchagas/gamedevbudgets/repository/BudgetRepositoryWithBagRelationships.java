package com.lucasxchagas.gamedevbudgets.repository;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BudgetRepositoryWithBagRelationships {
    Optional<Budget> fetchBagRelationships(Optional<Budget> budget);

    List<Budget> fetchBagRelationships(List<Budget> budgets);

    Page<Budget> fetchBagRelationships(Page<Budget> budgets);
}
