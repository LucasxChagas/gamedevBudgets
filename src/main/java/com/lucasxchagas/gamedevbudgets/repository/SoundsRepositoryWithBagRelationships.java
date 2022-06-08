package com.lucasxchagas.gamedevbudgets.repository;

import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SoundsRepositoryWithBagRelationships {
    Optional<Sounds> fetchBagRelationships(Optional<Sounds> sounds);

    List<Sounds> fetchBagRelationships(List<Sounds> sounds);

    Page<Sounds> fetchBagRelationships(Page<Sounds> sounds);
}
