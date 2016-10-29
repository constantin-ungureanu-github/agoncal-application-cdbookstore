package org.agoncal.application.cdbookstore.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString(callSuper=true)
public class Musician extends Artist {
    @Column(name = "preferred_instrument")
    @Getter
    @Setter
    private String preferredInstrument;

    @ManyToMany
    @Getter
    @Setter
    private Set<CD> cds = new HashSet<>();
}
