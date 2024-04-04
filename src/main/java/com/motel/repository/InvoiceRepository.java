package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query(nativeQuery = true, value = "select * from invoice a inner join renter b on a.renter_id = b.renter_id inner join accounts c on b.account_id = c.account_id inner join invoice_status d on a.invoice_status_id = d.invoice_status_id where c.email = :email AND (d.invoice_status_id = 2 OR d.invoice_status_id = 3)")
    List<Invoice> findByAccountEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = "SELECT SUM(total_price) AS totalRevenue FROM invoice WHERE DATEPART(YEAR, create_date) = 2024 GROUP BY DATEPART(MONTH, create_date)")
    List<Object> getRevenueByMonth();

    @Query(nativeQuery = true, value = "SELECT SUM(total_price) AS total_revenue FROM invoice WHERE DATEPART(YEAR, create_date) = :year GROUP BY DATEPART(MONTH, create_date);")
    List<Object> findByYear(@Param("year") int year);

    @Query(nativeQuery = true, value = "SELECT DATEPART(YEAR, create_date) AS invoice_year FROM invoice GROUP BY DATEPART(YEAR, create_date)")
    List<Object> getRevenueByYear();

    @Query(nativeQuery = true, value = "SELECT DATEPART(MONTH, create_date) AS invoice_month FROM invoice WHERE DATEPART(YEAR, create_date) = :year GROUP BY DATEPART(MONTH, create_date)")
    List<Object> getMonth(@Param("year") int year);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS total from invoice WHERE invoice_status_id = 2")
    List<Object> getInvoiceStatus();

    @Query(nativeQuery = true, value = "SELECT mr.descriptions, ec.create_date, ec.total_price AS total FROM motel_room mr INNER JOIN renter re ON re.motel_room_id = mr.motel_room_id INNER JOIN invoice ec ON re.renter_id = ec.renter_id WHERE ec.invoice_status_id = 2")
    List<Object> getInvoiceStatusDetail();
}
