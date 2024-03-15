package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
