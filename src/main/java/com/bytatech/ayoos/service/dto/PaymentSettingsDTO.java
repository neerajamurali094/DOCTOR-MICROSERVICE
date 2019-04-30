package com.bytatech.ayoos.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PaymentSettings entity.
 */
public class PaymentSettingsDTO implements Serializable {

    private Long id;

    private Double amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentSettingsDTO paymentSettingsDTO = (PaymentSettingsDTO) o;
        if (paymentSettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentSettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentSettingsDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
