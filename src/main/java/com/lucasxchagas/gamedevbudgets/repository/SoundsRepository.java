package com.lucasxchagas.gamedevbudgets.repository;

import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sounds entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoundsRepository extends JpaRepository<Sounds, Long> {}
