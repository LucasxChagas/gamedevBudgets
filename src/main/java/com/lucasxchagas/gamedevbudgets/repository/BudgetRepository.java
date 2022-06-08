package com.lucasxchagas.gamedevbudgets.repository;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Budget entity.
 */
@Repository
public interface BudgetRepository extends BudgetRepositoryWithBagRelationships, JpaRepository<Budget, Long> {
    default Optional<Budget> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Budget> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Budget> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct budget from Budget budget left join fetch budget.game",
        countQuery = "select count(distinct budget) from Budget budget"
    )
    Page<Budget> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct budget from Budget budget left join fetch budget.game")
    List<Budget> findAllWithToOneRelationships();

    @Query("select budget from Budget budget left join fetch budget.game where budget.id =:id")
    Optional<Budget> findOneWithToOneRelationships(@Param("id") Long id);
}
