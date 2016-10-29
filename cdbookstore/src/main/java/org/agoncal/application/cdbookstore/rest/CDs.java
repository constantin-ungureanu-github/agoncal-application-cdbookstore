package org.agoncal.application.cdbookstore.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.agoncal.application.cdbookstore.model.CD;

@XmlRootElement
@XmlSeeAlso(CD.class)
public class CDs extends ArrayList<CD> {
    private static final long serialVersionUID = 1L;

    public CDs() {
        super();
    }

    public CDs(final Collection<? extends CD> b) {
        super(b);
    }

    @XmlElement(name = "cd")
    public List<CD> getCDs() {
        return this;
    }
}
