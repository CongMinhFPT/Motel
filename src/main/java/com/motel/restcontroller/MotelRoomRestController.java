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

    

}
