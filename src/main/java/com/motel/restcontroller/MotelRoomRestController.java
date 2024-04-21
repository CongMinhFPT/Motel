package com.motel.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.model.MotelRoomsByPost;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.PostRepository;
import com.motel.service.MotelRoomService;

@RestController
public class MotelRoomRestController {
    @Autowired
    MotelRoomService motelRoomService;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Autowired
    PostRepository postRepository;

    // @GetMapping("/api/listMotelRoom")
    // public ResponseEntity<List<MotelRoomsByPost>> listMotelRoom() {
    // List<MotelRoomsByPost> byPosts = new ArrayList<>();
    // motelRoomRepository.findMotelRoomsByPost().forEach(a -> {
    // MotelRoomsByPost byPostw = new MotelRoomsByPost(a);
    // byPosts.add(byPostw);
    // });

    // return ResponseEntity.ok(byPosts);
    // }

    @GetMapping("/api/listMotelRoom")
    public ResponseEntity<Page<MotelRoom>> listMotelRoom(
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<MotelRoom> motelRoomsPage = motelRoomRepository.findMotelRoomsByPost(pageable);
        // Page<MotelRoomsByPost> motelRoomsByPostPage =
        // motelRoomsPage.map(MotelRoomsByPost::new);
        return ResponseEntity.ok(motelRoomsPage);
    }

    @GetMapping("/api/rooms")
    public Page<Post> getAllRooms(@RequestParam(defaultValue = "0") int page) {
        return postRepository.findAll(PageRequest.of(page, 5));
    }

    @GetMapping("/api/motelRoom/{motelRoomId}")
    public ResponseEntity<Optional<MotelRoom>> listMotelRoom(@PathVariable("motelRoomId") Integer motelRoomId) {
        return ResponseEntity.ok(motelRoomService.getById(motelRoomId));
    }

}
