package com.motel.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.motel.entity.FavoriteRoom;
import com.motel.entity.Image;
@Transactional
public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Modifying
@Query("DELETE FROM Image i WHERE i.motelRoom IS NULL")
void deleteAllByMotelRoomIsNull();
}
