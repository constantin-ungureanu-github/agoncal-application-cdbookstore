package org.agoncal.application.cdbookstore.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;

import org.agoncal.application.cdbookstore.util.Language;
import org.agoncal.application.cdbookstore.util.LanguageConverter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString
public class Author extends Artist {
    @Column(name = "preferred_language")
    @Convert(converter = LanguageConverter.class)
    @Getter
    @Setter
    private Language preferredLanguage;

    public Author(final String firstName, final String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }
}
