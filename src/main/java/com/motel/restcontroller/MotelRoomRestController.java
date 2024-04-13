package com.motel.restcontroller;

import java.util.ArrayList;
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
import com.motel.model.MotelRoomsByPost;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.MotelRoomService;

@RestController
public class MotelRoomRestController {
    @Autowired
    MotelRoomService motelRoomService;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @GetMapping("/api/listMotelRoom")
    public ResponseEntity<List<MotelRoomsByPost>> listMotelRoom() {
        List<MotelRoomsByPost> byPosts = new ArrayList<>();
        motelRoomRepository.findMotelRoomsByPost().forEach(a -> {
            MotelRoomsByPost byPostw = new MotelRoomsByPost(a);
            byPosts.add(byPostw);
        });
        return ResponseEntity.ok(byPosts);
    }

    @GetMapping("/api/motelRoom/{motelRoomId}")
    public ResponseEntity<Optional<MotelRoom>> listMotelRoom(@PathVariable("motelRoomId") Integer motelRoomId) {
        return ResponseEntity.ok(motelRoomService.getById(motelRoomId));
    }

}
