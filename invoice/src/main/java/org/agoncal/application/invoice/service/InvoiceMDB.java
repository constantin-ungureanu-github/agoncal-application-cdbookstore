package org.agoncal.application.invoice.service;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.agoncal.application.invoice.model.Invoice;
import org.slf4j.Logger;

@MessageDriven(mappedName = "invoiceQueue", activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/invoiceQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"), })
public class InvoiceMDB implements MessageListener {

    @Inject
    private InvoiceService invoiceService;

    @Inject
    private Logger logger;

    @Override
    public void onMessage(final Message message) {
        try {
            logger.info("Message received " + message);
            final Invoice invoice = message.getBody(Invoice.class);
            invoiceService.persist(invoice);
            logger.info("Invoice persisted " + invoice);
        } catch (final JMSException e) {
            logger.error("Cannot persist invoice", e);
        }
    }
}
