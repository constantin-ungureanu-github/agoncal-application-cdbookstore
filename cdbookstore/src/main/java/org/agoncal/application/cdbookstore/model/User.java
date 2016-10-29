package org.agoncal.application.cdbookstore.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.agoncal.application.cdbookstore.util.PasswordUtils;
import org.agoncal.application.cdbookstore.util.UserRole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "t_user")
@NamedQueries({ @NamedQuery(name = User.FIND_BY_EMAIL, query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = User.FIND_BY_UUID, query = "SELECT u FROM User u WHERE u.uuid = :uuid"),
        @NamedQuery(name = User.FIND_BY_LOGIN, query = "SELECT u FROM User u WHERE u.login = :login"),
        @NamedQuery(name = User.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM User u WHERE u.login = :login AND u.password = :password"),
        @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u") })
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIND_BY_EMAIL = "User.findByEmail";
    public static final String FIND_BY_LOGIN = "User.findByLogin";
    public static final String FIND_BY_UUID = "User.findByUUID";
    public static final String FIND_BY_LOGIN_PASSWORD = "User.findByLoginAndPassword";
    public static final String FIND_ALL = "User.findAll";

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

    @Column(length = 10, nullable = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Getter
    @Setter
    private String login;

    @Column(length = 50, name = "first_name", nullable = false)
    @NotNull
    @Size(min = 2, max = 50)
    @Getter
    @Setter
    private String firstName;

    @Column(length = 50, name = "last_name", nullable = false)
    @NotNull
    @Size(min = 2, max = 50)
    @Getter
    @Setter
    private String lastName;

    @Column
    @Getter
    @Setter
    private String telephone;

    @Column
    @NotNull
    @Getter
    @Setter
    private String email;

    @Column(length = 256, nullable = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Getter
    @Setter
    private String password;

    @Column(length = 256)
    @Size(min = 1, max = 256)
    @Getter
    @Setter
    private String uuid;

    @Enumerated
    @Column(name = "user_role")
    @Getter
    @Setter
    private UserRole role;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @Past
    @Getter
    @Setter
    private Date dateOfBirth;

    @PrePersist
    @PreUpdate
    private void digestPassword() {
        password = PasswordUtils.digestPassword(password);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
