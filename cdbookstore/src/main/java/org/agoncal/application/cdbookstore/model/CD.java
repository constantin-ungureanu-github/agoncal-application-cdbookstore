package org.agoncal.application.cdbookstore.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@XmlRootElement
@DiscriminatorValue("C")
@NoArgsConstructor
@ToString
public class CD extends Item {
    private static final long serialVersionUID = 1L;

    @Column(name = "nb_of_discs")
    @Getter
    @Setter
    private Integer nbOfDiscs;

    @ManyToOne
    @Getter
    @Setter
    private Label label;

    @ManyToMany
    @Getter
    @Setter
    private Set<Musician> musicians = new HashSet<>();

    @ManyToOne
    @Getter
    @Setter
    private Genre genre;
}
