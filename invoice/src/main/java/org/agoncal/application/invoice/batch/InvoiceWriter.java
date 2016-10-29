package org.agoncal.application.invoice.batch;

import java.io.File;
import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;

@Named
public class InvoiceWriter extends AbstractItemWriter {

    @Inject
    private JobContext jobContext;

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    @Override
    public void writeItems(final List<Object> items) throws Exception {
        final InvoiceSummaries summaries = new InvoiceSummaries();
        summaries.setYear(2016);

        final List<InvoiceSummary> invoiceSummaries = (List<InvoiceSummary>) items.get(0);
        for (final InvoiceSummary invoiceSummary : invoiceSummaries) {
            summaries.add(invoiceSummary);
        }

        final JAXBContext context = JAXBContext.newInstance(InvoiceSummaries.class);
        final Marshaller m = context.createMarshaller();
        final File file = new File("invoice.xml");
        m.marshal(summaries, file);
        logger.info("Wrote " + file.getAbsolutePath());
    }
}
