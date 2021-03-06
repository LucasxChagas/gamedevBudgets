package com.lucasxchagas.gamedevbudgets.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lucasxchagas.gamedevbudgets.domain.enumeration.SoundFormats;
import com.lucasxchagas.gamedevbudgets.domain.enumeration.SoundTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sounds.
 */
@Entity
@Table(name = "sounds")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sounds implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SoundTypes type;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private SoundFormats format;

    @ManyToMany(mappedBy = "sounds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sounds", "payments", "game" }, allowSetters = true)
    private Set<Budget> budgets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sounds id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Sounds name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SoundTypes getType() {
        return this.type;
    }

    public Sounds type(SoundTypes type) {
        this.setType(type);
        return this;
    }

    public void setType(SoundTypes type) {
        this.type = type;
    }

    public SoundFormats getFormat() {
        return this.format;
    }

    public Sounds format(SoundFormats format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(SoundFormats format) {
        this.format = format;
    }

    public Set<Budget> getBudgets() {
        return this.budgets;
    }

    public void setBudgets(Set<Budget> budgets) {
        if (this.budgets != null) {
            this.budgets.forEach(i -> i.removeSounds(this));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.addSounds(this));
        }
        this.budgets = budgets;
    }

    public Sounds budgets(Set<Budget> budgets) {
        this.setBudgets(budgets);
        return this;
    }

    public Sounds addBudget(Budget budget) {
        this.budgets.add(budget);
        budget.getSounds().add(this);
        return this;
    }

    public Sounds removeBudget(Budget budget) {
        this.budgets.remove(budget);
        budget.getSounds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sounds)) {
            return false;
        }
        return id != null && id.equals(((Sounds) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sounds{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", format='" + getFormat() + "'" +
            "}";
    }
}
