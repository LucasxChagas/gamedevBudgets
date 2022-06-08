package com.lucasxchagas.gamedevbudgets.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lucasxchagas.gamedevbudgets.domain.Budget} entity.
 */
public class BudgetDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private ZonedDateTime createdAt;

    private Set<SoundsDTO> sounds = new HashSet<>();

    private GameDTO game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<SoundsDTO> getSounds() {
        return sounds;
    }

    public void setSounds(Set<SoundsDTO> sounds) {
        this.sounds = sounds;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetDTO)) {
            return false;
        }

        BudgetDTO budgetDTO = (BudgetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, budgetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", sounds=" + getSounds() +
            ", game=" + getGame() +
            "}";
    }
}
