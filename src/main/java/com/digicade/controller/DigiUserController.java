package com.digicade.controller;

import com.digicade.domain.DigiUser;
import com.digicade.service.DigiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DigiUserController {

    @Autowired
    private DigiUserService digiUserService;

    @GetMapping("/digiusers/{id}")
    public ResponseEntity<DigiUser> getDigiUserById(@PathVariable Long id) {
        DigiUser digiUser = digiUserService.findDigiUserById(id);

        return new ResponseEntity<>(digiUser, HttpStatus.OK);
    }
}
