package org.agoncal.application.invoice.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.agoncal.application.invoice.model.Invoice;
import org.agoncal.application.invoice.service.InvoiceService;

@WebServlet(name = "InvoiceServlet", urlPatterns = "invoice")
public class InvoiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private InvoiceService invoiceService;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        // Handle the request
        final Long id = Long.valueOf(request.getParameter("id"));

        // Invoke the business logic
        final Invoice invoice = invoiceService.findById(id);

        // Set response content type
        response.setContentType("text/html");
        final PrintWriter out = response.getWriter();

        if (invoice == null) {
            out.print("No invoice with such id");
        } else {
            out.print(invoice);
        }
    }
}
