package org.agoncal.application.invoice.batch;

import java.util.ArrayList;
import java.util.List;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.agoncal.application.invoice.model.Invoice;
import org.slf4j.Logger;

@Named
public class InvoiceProcessor implements ItemProcessor {

    @Inject
    private JobContext jobContext;

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    @Override
    public Object processItem(final Object item) throws Exception {
        final List<Invoice> invoices = (List<Invoice>) item;
        final List<InvoiceSummary> summaries = new ArrayList<>();

        for (int i = 0; i < invoices.size(); i++) {
            final int currentMonth = invoices.get(i).getMonth();
            logger.info("currentMonth " + currentMonth);
            final InvoiceSummary summary = new InvoiceSummary(currentMonth + 1);
            int nbOfInvoices = 0;
            float total = 0F;
            while ((currentMonth == invoices.get(i).getMonth()) && (i < (invoices.size() - 1))) {
                nbOfInvoices++;
                total += invoices.get(i).getTotalAfterVat();
                logger.info("current invoice id " + invoices.get(i).getId());
                i++;
            }
            summary.setNumberOfInvoices(nbOfInvoices);
            summary.setTotal(total);
            summaries.add(summary);
        }

        logger.info("Processed {} summaries", summaries.size());
        return summaries;
    }
}
