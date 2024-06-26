package com.motel.restcontroller;

import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.FavoriteRoom;
import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
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
    
    @Autowired
    FavoriteRoomRepository favoriteRoomRepository;

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

    @GetMapping("/api/listFavoriteRoom/{account_id)")
    public ResponseEntity<List<FavoriteRoom>> getPagedProducts(@PathVariable("account_id") Integer account_id) {
        List<FavoriteRoom> favoriteRoomPage = favoriteRoomRepository.findFavoriteRoomByAccount(account_id);
        return ResponseEntity.ok(favoriteRoomPage);
    }

    // @GetMapping("/api/listFavoriteRoom")
    // public Page<MotelRoom> listFavoriteRooms(@RequestParam(defaultValue = "0")
    // int page,
    // @RequestParam(defaultValue = "5") int size) {
    // Pageable pageable = PageRequest.of(page, size);
    // return favoriteRoomService.getPagedFavoriteRooms(pageable);
    // }

}
