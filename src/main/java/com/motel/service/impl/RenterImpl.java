package com.motel.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.Indexs;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.entity.RoomStatus;
import com.motel.model.RenterModel;
import com.motel.repository.AccountsRepository;
import com.motel.repository.IndexsRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.RenterRepository;
import com.motel.repository.RoomStatusRepository;
import com.motel.service.RenterService;

@Service
public class RenterImpl implements RenterService {

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    RoomStatusRepository roomStatusRepository;

    @Autowired
    IndexsRepository indexsRepository;

    @Override
    public List<Account> getByPhoneNumber(String phone) {
        return accountsRepository.getByPhone(phone);
    }

    @Override
    public Renter addRenter(RenterModel renterModel) {

        Integer motelRoomId = renterModel.getMotelRoomId();

        Account account = accountsRepository.findById(renterModel.getAccountId()).orElse(null);

        MotelRoom motelRoom = motelRoomRepository.findById(renterModel.getMotelRoomId()).orElse(null);

        if (motelRoomId == null) {
            throw new IllegalStateException("Phòng trọ không được để trống.");
        }

        LocalDate renterDate = renterModel.getRenterDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        if (renterDate == null) {
            throw new IllegalArgumentException("Ngày thuê không được để trống.");
        }
        if (renterDate.isBefore(currentDate)) {
            throw new IllegalArgumentException("Ngày thuê phải từ ngày hôm nay trở đi.");
        }

        List<Account> accountsByMotel = accountsRepository.findAccountsMotel(renterModel.getAccountId());
        if (!accountsByMotel.isEmpty()) {
            throw new IllegalStateException("Tài khoản đã thuê ở nhà trọ khác.");
        }

        Renter existingRenter = renterRepository.findByAccount(account);
        if (existingRenter != null) {
            if (existingRenter.getMotelRoom().equals(motelRoom)) {
                throw new IllegalStateException("Tài khoản đã thuê ở phòng trọ này trước đó.");
            } else {
                throw new IllegalStateException("Tài khoản đã thuê ở một phòng trọ khác trước đó.");
            }
        }

        List<RoomStatus> roomStatuses = roomStatusRepository.findAll();
        motelRoom.setRoomStatus(roomStatuses.get(1));

        List<Renter> renters = renterRepository.findRenterByMotelRoom(motelRoomId);
        if (motelRoom.getCategoryRoom().getQuantity() < renters.size() + 1) {
            throw new IllegalStateException("Số người thuê đã vượt quá số lượng trong danh mục phòng!");
        }

        Indexs indexs = new Indexs();

        if (renters.size() == 0) {
            indexs.setCreateDate(new Date());
            indexs.setElectricityIndex(0.0);
            indexs.setWaterIndex(0.0);
            indexs.setMotelRoom(motelRoom);
            indexsRepository.save(indexs);
        }

        Renter renter = new Renter();
        renter.setAccount(account);
        renter.setMotelRoom(motelRoom);
        renter.setRenterDate(renterModel.getRenterDate());
        renter.setCheckOutDate(null);
        renter.setChangeRoomDate(null);

        renterRepository.save(renter);

        return renter;
    }

    @Override
    public List<Renter> getRenters() {
        return renterRepository.findAll();
    }

    @Override
    public Renter getRenter(Integer renterId) {
        return renterRepository.getById(renterId);
    }

    @Override
    public Renter updateRenter(RenterModel renterModel, Integer renterId) {
        Renter renter = renterRepository.findById(renterId).get();
        renter.setAccount(accountsRepository.getById(renterModel.getAccountId()));
        renter.setMotelRoom(motelRoomRepository.getById(renterModel.getMotelRoomId()));
        renter.setRenterDate(renterModel.getRenterDate());

        renterRepository.save(renter);
        return renter;
    }

    @Override
    public void deleteRenter(Integer renterId) {
        List<Renter> renters = renterRepository.findAll();
        Renter renter = renterRepository.findById(renterId).get();
        MotelRoom motelRoom = motelRoomRepository.findById(renter.getMotelRoom().getMotelRoomId()).orElse(null);
        System.out.println(renter.getAccount().getPhone());
        renter.setCheckOutDate(new Date());
        renterRepository.save(renter);
        if (motelRoomRepository.findMotelRoomsRenter(motelRoom.getMotelRoomId()).size() == 0) {
            List<RoomStatus> roomStatuses = roomStatusRepository.findAll();
            motelRoom.setRoomStatus(roomStatuses.get(0));
        }
    }

    @Override
    public List<MotelRoom> getMotelRoomByAccount(Integer accountId) {
        return motelRoomRepository.findMotelRoomsByAccount(accountId);
    }

    @Override
    public long countByMotelRoom(MotelRoom motelRoom) {
        // TODO Auto-generated method stub
        return renterRepository.countByMotelRoom(motelRoom);
    }

    @Override
    public void deleteRenterByMotelRoom(Integer motelRoomId) {
        MotelRoom motelRoom = motelRoomRepository.findById(motelRoomId).orElse(null);

        List<Renter> renters = renterRepository.findRenterByMotelRoom(motelRoomId);

        for (Renter renter : renters) {
            renter.setCheckOutDate(new Date());
        }
        renterRepository.saveAll(renters);

        List<RoomStatus> roomStatuses = roomStatusRepository.findAll();
        motelRoom.setRoomStatus(roomStatuses.get(0));
        renterRepository.saveAll(renters);
    }

    @Override
    public Renter changeRoom(RenterModel renterModel, Integer renterId) {

        if (renterId == null) {
            throw new IllegalStateException("ID không được để trống.");
        }

        Renter renter1 = renterRepository.findById(renterId).get();
        MotelRoom motelRoom1 = motelRoomRepository.findById(renter1.getMotelRoom().getMotelRoomId()).orElse(null);
        System.out.println(renter1.getAccount().getPhone());
        renter1.setCheckOutDate(new Date());
        renterRepository.save(renter1);

        if (motelRoomRepository.findMotelRoomsRenter(motelRoom1.getMotelRoomId()).size() == 0) {
            List<RoomStatus> roomStatuses = roomStatusRepository.findAll();
            motelRoom1.setRoomStatus(roomStatuses.get(0));
        }

        Integer motelRoomId = renterModel.getMotelRoomId();

        Account account = accountsRepository.findById(renter1.getAccount().getAccountId()).orElse(null);

        MotelRoom motelRoom = motelRoomRepository.findById(renterModel.getMotelRoomId()).orElse(null);

        if (motelRoomId == null) {
            throw new IllegalStateException("Phòng trọ không được để trống.");
        }

        if (motelRoomRepository.findMotelRoomsRenter(motelRoom.getMotelRoomId()).size() == 0) {
            List<RoomStatus> roomStatuses1 = roomStatusRepository.findAll();
            motelRoom.setRoomStatus(roomStatuses1.get(0));
        } else {
            List<RoomStatus> roomStatuses2 = roomStatusRepository.findAll();
            motelRoom.setRoomStatus(roomStatuses2.get(1));
        }

        List<Renter> renters = renterRepository.findRenterByMotelRoom(motelRoomId);
        if (motelRoom.getCategoryRoom().getQuantity() < renters.size() + 1) {
            throw new IllegalStateException("Số người thuê đã vượt quá số lượng trong danh mục phòng!");
        }

        Indexs indexs = new Indexs();

        if (renters.size() == 0) {
            indexs.setCreateDate(new Date());
            indexs.setElectricityIndex(0.0);
            indexs.setWaterIndex(0.0);
            indexs.setMotelRoom(motelRoom);
            indexsRepository.save(indexs);
        }

        Renter renter = new Renter();
        renter.setAccount(account);
        renter.setMotelRoom(motelRoom);
        renter.setRenterDate(new Date());
        renter.setCheckOutDate(null);
        renter.setChangeRoomDate(new Date());

        renterRepository.save(renter);

        return renter;

    }
}
