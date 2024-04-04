package com.motel.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.Invoice;
import com.motel.entity.InvoiceStatus;
import com.motel.entity.Renter;
import com.motel.model.InvoiceModel;
import com.motel.repository.InvoiceRepository;
import com.motel.repository.InvoiceStatusRepository;
import com.motel.repository.RenterRepository;
import com.motel.service.InvoiceService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class InvoiceRestController {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    InvoiceStatusRepository invoiceStatusRepository;

    @GetMapping("/api/renter-list")
    public ResponseEntity<List<Renter>> listRenter() {
        return ResponseEntity.ok(renterRepository.findAll());
    }

    @GetMapping("/api/invoice-status-list")
    public ResponseEntity<List<InvoiceStatus>> listInvoiceStatus() {
        return ResponseEntity.ok(invoiceStatusRepository.findAll());
    }

    @GetMapping("/api/invoice-list/{invoiceId}")
    public ResponseEntity<Optional<Invoice>> listInvoice(@PathVariable("invoiceId") Integer invoiceId) {
        return ResponseEntity.ok(invoiceService.getById(invoiceId));
    }

    @PostMapping("/api/invoice/add-invoice")
    public ResponseEntity<Invoice> addInvoice(@RequestBody InvoiceModel invoiceModel) {
        return ResponseEntity.ok(invoiceService.addInvoice(invoiceModel));
    }

}
