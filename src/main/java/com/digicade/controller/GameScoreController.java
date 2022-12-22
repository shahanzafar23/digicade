package com.digicade.controller;

import com.digicade.domain.GameScore;
import com.digicade.domain.User;
import com.digicade.service.GameScoreService;
import com.digicade.service.UserService;
import com.digicade.service.dto.GameScoreDTO;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameScoreController {

    @Autowired
    private GameScoreService gameScoreService;

    @Autowired
    private UserService userService;

    @PostMapping("/save-game-scores")
    public ResponseEntity<GameScoreDTO> saveGameScores(@RequestBody GameScoreDTO gameScoreDTO) {
        GameScoreDTO save = gameScoreService.save(gameScoreDTO);

        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}
