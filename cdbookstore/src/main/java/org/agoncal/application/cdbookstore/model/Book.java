package org.agoncal.application.cdbookstore.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.agoncal.application.cdbookstore.util.Language;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("B")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@ToString
public class Book extends Item {
    private static final long serialVersionUID = 1L;

    @Column(length = 50)
    @Getter
    @Setter
    private String isbn;

    @Column(name = "nb_of_pages")
    @Min(1)
    @XmlElement(name = "pages")
    @Getter
    @Setter
    private Integer nbOfPage;

    @Column(name = "publication_date")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date publicationDate;

    @Enumerated
    @Getter
    @Setter
    private Language language;

    @ManyToOne
    @Getter
    @Setter
    private Publisher publisher;

    @ManyToOne
    @Getter
    @Setter
    private Category category;

    @OneToMany
    @Getter
    @Setter
    private Set<Author> authors = new HashSet<>();

    public Book(final String isbn, final String title, final String description, final Float unitCost, final Integer nbOfPages, final Language language) {
        this.isbn = isbn;
        setTitle(title);
        setDescription(description);
        setUnitCost(unitCost);
        nbOfPage = nbOfPages;
        this.language = language;
    }

    public void addAuthor(final Author author) {
        authors.add(author);
    }
}
