package com.motel.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.model.RenterModel;
import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.RenterRepository;
import com.motel.service.RenterService;

@Service
public class RenterImpl implements RenterService {

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Autowired
    RenterRepository renterRepository;

    @Override
    public List<Account> getByPhoneNumber(String phone) {
        return accountsRepository.getByPhone(phone);
    }

    @Override
    public List<MotelRoom> getAll() {
        return motelRoomRepository.findAll();
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

        Renter existingRenter = renterRepository.findByAccount(account);
        if (existingRenter != null) {
            if (existingRenter.getMotelRoom().equals(motelRoom)) {
                throw new IllegalStateException("Tài khoản đã thuê ở phòng trọ này trước đó.");
            } else {
                throw new IllegalStateException("Tài khoản đã thuê ở một phòng trọ khác trước đó.");
            }
        }

        Renter renter = new Renter();
        renter.setAccount(account);
        renter.setMotelRoom(motelRoom);
        renter.setRenterDate(renterModel.getRenterDate());

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
        if (renterId == null) {
            throw new IllegalStateException("Nguời thuê không tồn tại!");
        } else {
            renterRepository.deleteById(renterId);
        }
    }
}
