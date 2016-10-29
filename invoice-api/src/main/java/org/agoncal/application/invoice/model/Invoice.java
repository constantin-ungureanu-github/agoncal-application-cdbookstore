package org.agoncal.application.invoice.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@NamedQuery(name = Invoice.FIND_MONTHLY, query = "SELECT i FROM Invoice i ORDER BY i.invoiceDate ASC")
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIND_MONTHLY = "Invoice.findMonthly";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @Column(name = "invoice_date", updatable = false)
    @Temporal(TemporalType.DATE)
    @Past
    private Date invoiceDate;
    private Float totalBeforeDiscount;

    @Column(name = "discount_rate")
    private Float discountRate;
    private Float discount;
    private Float totalAfterDiscount;

    @Column(name = "vat_rate")
    private Float vatRate;
    private Float vat;
    private Float totalAfterVat;

    @Column(length = 50, name = "first_name", nullable = false)
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @Column(length = 50, name = "last_name", nullable = false)
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;
    private String telephone;

    @Column
    @NotNull
    private String email;

    @Column(length = 50, nullable = false)
    @Size(min = 5, max = 50)
    @NotNull
    private String street1;
    private String street2;

    @Column(length = 50, nullable = false)
    @Size(min = 2, max = 50)
    @NotNull
    private String city;
    private String state;

    @Column(length = 10, name = "zip_code", nullable = false)
    @Size(min = 1, max = 10)
    @NotNull
    private String zipcode;
    private String country;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<InvoiceLine> invoiceLines = new HashSet<>();

    public Invoice() {
    }

    public Invoice(final String firstName, final String lastName, final String email, final String street1, final String city, final String zipcode,
            final String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.street1 = street1;
        this.city = city;
        this.country = country;
        this.zipcode = zipcode;
    }

    public Invoice(final String firstName, final String lastName, final String email, final String street1, final String city, final String zipcode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.street1 = street1;
        this.city = city;
        this.zipcode = zipcode;
    }

    public Invoice(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(final Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getMonth() {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(invoiceDate);
        return cal.get(Calendar.MONTH);
    }

    public Float getTotalBeforeDiscount() {
        return totalBeforeDiscount;
    }

    public void setTotalBeforeDiscount(final Float totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    public Float getVatRate() {
        return vatRate;
    }

    public void setVatRate(final Float vatRate) {
        this.vatRate = vatRate;
    }

    public Float getVat() {
        return vat;
    }

    public void setVat(final Float vat) {
        this.vat = vat;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(final Float discount) {
        this.discount = discount;
    }

    public Float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(final Float discountRate) {
        this.discountRate = discountRate;
    }

    public Float getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public void setTotalAfterDiscount(final Float totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
    }

    public Float getTotalAfterVat() {
        return totalAfterVat;
    }

    public void setTotalAfterVat(final Float totalAfterVat) {
        this.totalAfterVat = totalAfterVat;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(final String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(final String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(final String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(final String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public Set<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(final Set<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public void addInvoiceLine(final InvoiceLine invoiceLine) {
        invoiceLines.add(invoiceLine);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        final Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceDate, invoice.invoiceDate) && Objects.equals(firstName, invoice.firstName) && Objects.equals(lastName, invoice.lastName)
                && Objects.equals(email, invoice.email) && Objects.equals(invoiceLines, invoice.invoiceLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceDate, firstName, lastName, email, invoiceLines);
    }

    @Override
    public String toString() {
        return "Invoice{" + "city='" + city + '\'' + ", id=" + id + ", version=" + version + ", invoiceDate=" + invoiceDate + ", totalBeforeDiscount="
                + totalBeforeDiscount + ", discountRate=" + discountRate + ", discount=" + discount + ", totalAfterDiscount=" + totalAfterDiscount
                + ", vatRate=" + vatRate + ", vat=" + vat + ", totalAfterVat=" + totalAfterVat + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
                + '\'' + ", telephone='" + telephone + '\'' + ", email='" + email + '\'' + ", street1='" + street1 + '\'' + ", street2='" + street2 + '\''
                + ", state='" + state + '\'' + ", zipcode='" + zipcode + '\'' + ", country='" + country + '\'' + ", invoiceLines=" + invoiceLines + '}';
    }
}
