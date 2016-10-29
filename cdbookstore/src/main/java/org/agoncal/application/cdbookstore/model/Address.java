package org.agoncal.application.cdbookstore.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @Size(min = 5, max = 50)
    @NotNull
    @Getter
    @Setter
    private String street1;

    @Getter
    @Setter
    private String street2;

    @Size(min = 2, max = 50)
    @NotNull
    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String state;

    @Size(min = 1, max = 10)
    @NotNull
    @Getter
    @Setter
    private String zipcode;
}
