package com.motel.service;

import java.util.List;

import com.motel.entity.Account;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.model.RenterModel;

public interface RenterService {
    List<Account> getByPhoneNumber(String phone);

    List<MotelRoom> getMotelRoomByAccount(Integer accountId);

    Renter addRenter(RenterModel renterModel);

    Renter updateRenter(RenterModel renterModel, Integer renterId);

    List<Renter> getRenters();

    Renter getRenter(Integer renterId);

    void deleteRenter(Integer renterId);

    void deleteRenterByMotelRoom(Integer motelRoomId);

    long countByMotelRoom(MotelRoom motelRoom);

    Renter changeRoom(RenterModel renterModel, Integer renterId);
}
