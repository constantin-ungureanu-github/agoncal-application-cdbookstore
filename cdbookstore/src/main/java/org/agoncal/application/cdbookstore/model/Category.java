package org.agoncal.application.cdbookstore.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@ToString
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @XmlTransient
    @Getter
    @Setter
    private Long id;

    @Version
    @Column(name = "version")
    @XmlTransient
    @Getter
    @Setter
    private int version;

    @Column(length = 100)
    @NotNull
    @Size(max = 100)
    @XmlAttribute
    @Getter
    @Setter
    private String name;

    public Category(final String name) {
        setName(name);
    }
}
