package com.digicade.controller;

import com.digicade.service.GameService;
import com.digicade.service.dto.GameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/save-game")
    public ResponseEntity<GameDTO> saveGame(@RequestBody GameDTO gameDTO) {
        GameDTO save = gameService.save(gameDTO);

        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}
