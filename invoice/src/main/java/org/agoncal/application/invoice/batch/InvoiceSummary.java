package org.agoncal.application.invoice.batch;

public class InvoiceSummary {
    private Integer month;
    private Integer numberOfInvoices;
    private Float total;

    public InvoiceSummary() {
    }

    public InvoiceSummary(final Integer month) {
        this.month = month;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(final Integer month) {
        this.month = month;
    }

    public Integer getNumberOfInvoices() {
        return numberOfInvoices;
    }

    public void setNumberOfInvoices(final Integer numberOfInvoices) {
        this.numberOfInvoices = numberOfInvoices;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(final Float total) {
        this.total = total;
    }
}
