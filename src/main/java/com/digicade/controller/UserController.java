package com.digicade.controller;

import com.digicade.domain.User;
import com.digicade.service.UserService;
import com.digicade.service.dto.LeaderBoard;
import com.digicade.service.dto.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User user1 = userService.saveUser(user);

        return new ResponseEntity<>(user1, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user-profile/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        UserProfileDTO userProfile = userService.getUserProfile(id);

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @GetMapping("/leader-board/{id}")
    public ResponseEntity<LeaderBoard> getLeaderBoardByUserId(@PathVariable Long id) {
        LeaderBoard leaderBoard = userService.getLeaderBoardByUserId(id);

        return new ResponseEntity<>(leaderBoard, HttpStatus.OK);
    }
}
