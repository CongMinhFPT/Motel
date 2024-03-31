package com.motel.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query(nativeQuery = true, value = "select * from invoice a inner join renter b on a.renter_id = b.renter_id inner join accounts c on b.account_id = c.account_id inner join invoice_status d on a.invoice_status_id = d.invoice_status_id where c.email = :email AND (d.invoice_status_id = 2 OR d.invoice_status_id = 3)")
    List<Invoice> findByAccountEmail(@Param("email") String email);
}
