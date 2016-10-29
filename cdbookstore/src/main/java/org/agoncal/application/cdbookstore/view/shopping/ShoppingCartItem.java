package org.agoncal.application.cdbookstore.view.shopping;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.agoncal.application.cdbookstore.model.Item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ShoppingCartItem {
    @NotNull
    @Getter
    @Setter
    private Item item;

    @NotNull
    @Min(1)
    @Getter
    @Setter
    private Integer quantity;

    public ShoppingCartItem(final Item item, final Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Float getSubTotal() {
        return item.getUnitCost() * quantity;
    }
}
