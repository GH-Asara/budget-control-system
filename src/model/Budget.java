package model;

import java.util.Objects;
import java.util.UUID;

public class Budget {
    private String id;
    private String description;
    private Integer amount;

    public Budget(String id, String description, Integer amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    public Budget(String description, Integer amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget that = (Budget) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, amount);
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
