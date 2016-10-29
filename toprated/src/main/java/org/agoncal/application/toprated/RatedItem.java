package org.agoncal.application.toprated;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rated_item")
@NamedQueries({ @NamedQuery(name = RatedItem.FIND_TOP_ITEMS, query = "SELECT i FROM RatedItem i WHERE i.rank = 5") })
@EqualsAndHashCode
@ToString
public class RatedItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String FIND_TOP_ITEMS = "TopItem.findTopRated";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    @Setter
    protected Long id;

    @Column(length = 200)
    @NotNull
    @Size(min = 1, max = 200)
    @Getter
    @Setter
    protected String title;

    @Getter
    @Setter
    protected Integer rank;
}
