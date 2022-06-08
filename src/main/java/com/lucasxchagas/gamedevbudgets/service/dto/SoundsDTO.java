package com.lucasxchagas.gamedevbudgets.service.dto;

import com.lucasxchagas.gamedevbudgets.domain.enumeration.SoundFormats;
import com.lucasxchagas.gamedevbudgets.domain.enumeration.SoundTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lucasxchagas.gamedevbudgets.domain.Sounds} entity.
 */
public class SoundsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private SoundTypes type;

    private SoundFormats format;

    private Set<BudgetDTO> budgets = new HashSet<>();

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

    public SoundTypes getType() {
        return type;
    }

    public void setType(SoundTypes type) {
        this.type = type;
    }

    public SoundFormats getFormat() {
        return format;
    }

    public void setFormat(SoundFormats format) {
        this.format = format;
    }

    public Set<BudgetDTO> getBudgets() {
        return budgets;
    }

    public void setBudgets(Set<BudgetDTO> budgets) {
        this.budgets = budgets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SoundsDTO)) {
            return false;
        }

        SoundsDTO soundsDTO = (SoundsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, soundsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SoundsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", format='" + getFormat() + "'" +
            ", budgets=" + getBudgets() +
            "}";
    }
}
