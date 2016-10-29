package org.agoncal.application.invoice.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "invoice_line")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class InvoiceLine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(length = 200)
    @NotNull
    @Size(min = 1, max = 200)
    protected String title;

    @Column(name = "unit_cost")
    @NotNull
    @Min(1)
    protected Float unitCost;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;

    public InvoiceLine(final Integer quantity, final String title, final Float unitCost) {
        this.quantity = quantity;
        this.title = title;
        this.unitCost = unitCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Float getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(final Float unitCost) {
        this.unitCost = unitCost;
    }
}
