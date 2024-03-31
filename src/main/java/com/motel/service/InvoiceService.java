package com.motel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;

import com.motel.entity.Invoice;
import com.motel.model.InvoiceModel;

public interface InvoiceService {
    List<Invoice> getAll();
    Invoice addInvoice(InvoiceModel invoiceModel);
    Optional<Invoice> getById(Integer invoiceId);
    Invoice updateInvoice(Integer invoiceId, Invoice invoice);
}
