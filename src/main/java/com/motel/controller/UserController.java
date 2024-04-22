package com.motel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.motel.entity.User;
import com.motel.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(
            @Payload User user
    ) {
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
    
//    @GetMapping("/users/messages/{nickname}")
//    public ResponseEntity<List<User>> findUsersWithMessages(@PathVariable String nickname) {
//        List<User> usersWithMessages = userService.findUsersWithMessages(nickname);
//        return ResponseEntity.ok(usersWithMessages);
//    }


    @GetMapping("/users/messages/{nickname}")
    public ResponseEntity<List<User>> findUsersWithMessages(@PathVariable String nickname) {
        List<User> usersWithMessages = userService.findUsersWithMessages(nickname);
        System.out.println("====================" + usersWithMessages);
        return ResponseEntity.ok(usersWithMessages);
    }
    
    @GetMapping("/allUser/{nickname}")
    public ResponseEntity<List<User>> allUser(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.allUser(nickname));
    }
}
