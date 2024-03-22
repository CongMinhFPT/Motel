package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.motel.entity.Motel;

public interface MotelRepository extends JpaRepository<Motel, Integer> {
//	@Query("SELECT mr.image, mr.address, mr.ward, mr.price, mr.length, mr.width, mr.descriptions, "
//		       + "a.fullname, mr.createDate, m.district, m.province "
//		       + "FROM MotelRoom mr "
//		       + "INNER JOIN mr.motel m "
//		       + "INNER JOIN m.account a "
//		       + "ORDER BY mr.price ASC") // hoặc DESC nếu giảm dần
//		List<Object[]> findAllRoomOrderByPriceAsc();
//
//		@Query("SELECT mr.image, mr.address, mr.ward, mr.price, mr.length, mr.width, mr.descriptions, "
//		       + "a.fullname, mr.createDate, m.district, m.province "
//		       + "FROM MotelRoom mr "
//		       + "INNER JOIN mr.motel m "
//		       + "INNER JOIN m.account a "
//		       + "ORDER BY mr.price DESC") // hoặc ASC nếu tăng dần
//		List<Object[]> findAllRoomOrderByPriceDesc();


}
