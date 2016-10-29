package org.agoncal.application.invoice.batch;

import java.util.List;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.agoncal.application.invoice.model.Invoice;
import org.slf4j.Logger;

@Named
public class InvoiceReader extends AbstractItemReader {
    @Inject
    private JobContext jobContext;

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    @Override
    public Object readItem() throws Exception {
        final TypedQuery<Invoice> query = entityManager.createNamedQuery(Invoice.FIND_MONTHLY, Invoice.class);
        final List<Invoice> invoices = query.getResultList();
        logger.info("Read {} invoices", invoices.size());
        return invoices;
    }
}
