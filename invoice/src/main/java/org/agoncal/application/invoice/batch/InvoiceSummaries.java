package org.agoncal.application.invoice.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "invoiceSummary")
@XmlType(propOrder = { "year", "invoiceSummaries" })
@XmlSeeAlso(InvoiceSummary.class)
public class InvoiceSummaries extends ArrayList<InvoiceSummary> {
    private static final long serialVersionUID = 1L;
    private Integer year;

    public InvoiceSummaries() {
        super();
    }

    public InvoiceSummaries(final Collection<? extends InvoiceSummary> c) {
        super(c);
    }

    @XmlElement(name = "invoiceSummaries")
    public List<InvoiceSummary> getInvoiceSummaries() {
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }
}