package com.digicade.controller;

import com.digicade.service.PlayerService;
import com.digicade.service.dto.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/save-player")
    public ResponseEntity<PlayerDTO> savePlayer(@RequestBody PlayerDTO playerDTO) {
        PlayerDTO save = playerService.save(playerDTO);

        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}
