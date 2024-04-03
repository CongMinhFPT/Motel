package com.motel.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.MotelRoom;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.MotelRoomService;

@RestController
public class MotelRoomRestController {
    @Autowired
    MotelRoomService motelRoomService;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @GetMapping("/api/listMotelRoom")
    public ResponseEntity<List<MotelRoom>> listMotelRoom() {
        return ResponseEntity.ok(motelRoomRepository.findMotelRoomsByPost());
    }

    @GetMapping("/api/motelRoom/{motelRoomId}")
    public ResponseEntity<Optional<MotelRoom>> listMotelRoom(@PathVariable("motelRoomId") Integer motelRoomId) {
        return ResponseEntity.ok(motelRoomService.getById(motelRoomId));
    }

}
