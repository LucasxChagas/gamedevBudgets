package com.lucasxchagas.gamedevbudgets.repository;

import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sounds entity.
 */
@Repository
public interface SoundsRepository extends JpaRepository<Sounds, Long> {
    default Optional<Sounds> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Sounds> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Sounds> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct sounds from Sounds sounds left join fetch sounds.bugdet",
        countQuery = "select count(distinct sounds) from Sounds sounds"
    )
    Page<Sounds> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct sounds from Sounds sounds left join fetch sounds.bugdet")
    List<Sounds> findAllWithToOneRelationships();

    @Query("select sounds from Sounds sounds left join fetch sounds.bugdet where sounds.id =:id")
    Optional<Sounds> findOneWithToOneRelationships(@Param("id") Long id);
}
