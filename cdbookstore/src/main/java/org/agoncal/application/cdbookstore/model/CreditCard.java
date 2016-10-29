package org.agoncal.application.cdbookstore.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.agoncal.application.cdbookstore.util.CreditCardType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class CreditCard implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 30)
    @Getter
    @Setter
    private String creditCardNumber;

    @NotNull
    @Getter
    @Setter
    private CreditCardType creditCardType;

    @NotNull
    @Size(min = 1, max = 5)
    @Getter
    @Setter
    private String creditCardExpDate;
}
