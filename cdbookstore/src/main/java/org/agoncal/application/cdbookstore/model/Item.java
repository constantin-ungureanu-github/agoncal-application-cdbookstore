package org.agoncal.application.cdbookstore.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("I")
@NamedQueries({ @NamedQuery(name = Item.FIND_TOP_RATED, query = "SELECT i FROM Item i WHERE i.id in :ids"),
        @NamedQuery(name = Item.SEARCH, query = "SELECT i FROM Item i WHERE UPPER(i.title) LIKE :keyword OR UPPER(i.description) LIKE :keyword ORDER BY i.title") })
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@ToString
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIND_TOP_RATED = "Item.findTopRated";
    public static final String SEARCH = "Item.search";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    @Setter
    private Long id;

    @Version
    @Column(name = "version")
    @XmlTransient
    @Getter
    @Setter
    private int version;

    @Column(length = 200)
    @NotNull
    @Size(min = 1, max = 200)
    @Getter
    @Setter
    private String title;

    @Column(length = 10000)
    @Size(min = 1, max = 10000)
    @Getter
    @Setter
    private String description;

    @Column(name = "unit_cost")
    @Min(1)
    @Getter
    @Setter
    private Float unitCost;

    @Getter
    @Setter
    private Integer rank;

    @Column(name = "small_image_url")
    @XmlElement(name = "small-image-url")
    @Getter
    @Setter
    private String smallImageURL;

    @Column(name = "medium_image_url")
    @XmlElement(name = "medium-image-url")
    @Getter
    @Setter
    private String mediumImageURL;
}
