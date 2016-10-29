package org.agoncal.application.cdbookstore.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Cacheable
@NamedQuery(name = Country.FIND_ALL, query = "SELECT c FROM Country c ORDER BY c.name")
@NoArgsConstructor
@ToString
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Country.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    @Setter
    private Long id;

    @Version
    @Column(name = "version")
    @Getter
    @Setter
    private int version;

    @Column(length = 2, name = "iso_code", nullable = false)
    @NotNull
    @Size(min = 2, max = 2)
    @Getter
    @Setter
    private String isoCode;

    @Column(length = 80, nullable = false)
    @NotNull
    @Size(min = 2, max = 80)
    @Getter
    @Setter
    private String name;

    @Column(length = 80, name = "printable_name", nullable = false)
    @NotNull
    @Size(min = 2, max = 80)
    @Getter
    @Setter
    private String printableName;

    @Column(length = 3)
    @NotNull
    @Size(min = 3, max = 3)
    @Getter
    @Setter
    private String iso3;

    @Column(length = 3)
    @NotNull
    @Size(min = 3, max = 3)
    @Getter
    @Setter
    private String numcode;
}
