package org.agoncal.application.cdbookstore.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@ToString
public class Artist {
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

    @Column(length = 50, name = "first_name", nullable = false)
    @NotNull
    @Size(min = 2, max = 50)
    @XmlElement(name = "first-name")
    @Getter
    @Setter
    private String firstName;

    @Column(length = 50, name = "last_name", nullable = false)
    @NotNull
    @Size(min = 2, max = 50)
    @XmlElement(name = "last-name")
    @Getter
    @Setter
    private String lastName;

    @Column(length = 5000)
    @Size(max = 5000)
    @Getter
    @Setter
    private String bio;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @Past
    @Getter
    @Setter
    private Date dateOfBirth;

    @Transient
    @Getter
    @Setter
    private Integer age;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void calculateAge() {
        if (dateOfBirth == null) {
            age = null;
            return;
        }

        final Calendar birth = new GregorianCalendar();
        birth.setTime(dateOfBirth);
        final Calendar now = new GregorianCalendar();
        now.setTime(new Date());
        int adjust = 0;
        if ((now.get(Calendar.DAY_OF_YEAR) - birth.get(Calendar.DAY_OF_YEAR)) < 0) {
            adjust = -1;
        }
        age = (now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)) + adjust;
    }
}
