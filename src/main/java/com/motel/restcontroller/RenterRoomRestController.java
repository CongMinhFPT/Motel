package com.motel.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.Account;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.model.RenterModel;
import com.motel.repository.AccountsRepository;
import com.motel.service.AccountService;
import com.motel.service.MotelRoomService;
import com.motel.service.RenterService;

@RestController
public class RenterRoomRestController {

    @Autowired
    RenterService renterService;

    @Autowired
    MotelRoomService motelRoomService;

    @Autowired
    AccountsRepository accountsRepository;

    @GetMapping("/api/search")
    public ResponseEntity<List<Account>> getByPhone(@RequestParam("phone") String phone) {
        return ResponseEntity.ok(renterService.getByPhoneNumber(phone));
    }

    @GetMapping("/api/motelRoom")
    public ResponseEntity<List<MotelRoom>> getAllMotelRoom() {
        return ResponseEntity.ok(renterService.getAll());
    }

    @PostMapping("/api/addRenter")
    public ResponseEntity<Renter> addRenter(@RequestBody RenterModel renterModel) {
        return ResponseEntity.ok(renterService.addRenter(renterModel));
    }
}
