package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.InvoiceStatus;

public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatus, Integer>{
    
}
