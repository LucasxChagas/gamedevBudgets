package com.lucasxchagas.gamedevbudgets.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Budget.
 */
@Entity
@Table(name = "budget")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @ManyToMany
    @JoinTable(
        name = "rel_budget__sounds",
        joinColumns = @JoinColumn(name = "budget_id"),
        inverseJoinColumns = @JoinColumn(name = "sounds_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "budgets" }, allowSetters = true)
    private Set<Sounds> sounds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "budgets" }, allowSetters = true)
    private Game game;

    @ManyToMany(mappedBy = "budgets")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "budgets" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Budget id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Budget name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Budget createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Sounds> getSounds() {
        return this.sounds;
    }

    public void setSounds(Set<Sounds> sounds) {
        this.sounds = sounds;
    }

    public Budget sounds(Set<Sounds> sounds) {
        this.setSounds(sounds);
        return this;
    }

    public Budget addSounds(Sounds sounds) {
        this.sounds.add(sounds);
        sounds.getBudgets().add(this);
        return this;
    }

    public Budget removeSounds(Sounds sounds) {
        this.sounds.remove(sounds);
        sounds.getBudgets().remove(this);
        return this;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Budget game(Game game) {
        this.setGame(game);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.removeBudget(this));
        }
        if (payments != null) {
            payments.forEach(i -> i.addBudget(this));
        }
        this.payments = payments;
    }

    public Budget payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Budget addPayment(Payment payment) {
        this.payments.add(payment);
        payment.getBudgets().add(this);
        return this;
    }

    public Budget removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.getBudgets().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Budget)) {
            return false;
        }
        return id != null && id.equals(((Budget) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Budget{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
