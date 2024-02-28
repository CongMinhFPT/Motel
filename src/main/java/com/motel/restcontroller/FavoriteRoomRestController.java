package com.motel.restcontroller;

import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.FavoriteRoom;
import com.motel.entity.MotelRoom;
import com.motel.repository.FavoriteRoomRepository;
import com.motel.service.FavoriteRoomService;
import com.motel.service.MotelRoomService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class FavoriteRoomRestController {
    @Autowired
    FavoriteRoomService favoriteRoomService;

    @Autowired
    MotelRoomService motelRoomService;

    @PostMapping("/api/addFavoriteRoom/{accountId}/{motelRoomId}")
    public ResponseEntity<FavoriteRoom> addFavoriteRoom(@PathVariable("accountId") Integer accountId,
            @PathVariable("motelRoomId") Integer motelRoomId) {
        return ResponseEntity.ok(favoriteRoomService.addRoom(accountId, motelRoomId));
    }

    @DeleteMapping("/api/deleteFavoriteRoom/{favoriteRoomId}")
    public ResponseEntity<Void> deleteFavoriteRoom(@PathVariable("favoriteRoomId") Integer favoriteRoomId) {
        favoriteRoomService.deleteRoom(favoriteRoomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/listFavoriteRoom")
    public ResponseEntity<Page<MotelRoom>> getPagedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "create_date") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<MotelRoom> favoriteRoomPage = favoriteRoomService.getPagedFavoriteRooms(pageable);
        return ResponseEntity.ok(favoriteRoomPage);
    }

}
