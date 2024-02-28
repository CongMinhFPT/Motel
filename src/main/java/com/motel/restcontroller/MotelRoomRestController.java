package com.motel.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.MotelRoom;
import com.motel.service.MotelRoomService;

@RestController
public class MotelRoomRestController {
    @Autowired MotelRoomService motelRoomService;

    @GetMapping("/api/listMotelRoom")
    public ResponseEntity<List<MotelRoom>> listMotelRoom(){
        return ResponseEntity.ok(motelRoomService.getAll());
    }
}
