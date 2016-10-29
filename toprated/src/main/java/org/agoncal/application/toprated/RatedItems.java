package org.agoncal.application.toprated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@XmlRootElement
@XmlSeeAlso(RatedItem.class)
public class RatedItems extends ArrayList<RatedItem> {
    private static final long serialVersionUID = 8850310546787663682L;

    public RatedItems() {
        super();
    }

    public RatedItems(final Collection<? extends RatedItem> c) {
        super(c);
    }

    @XmlElement(name = "topItem")
    public List<RatedItem> getTopItems() {
        return this;
    }
}
