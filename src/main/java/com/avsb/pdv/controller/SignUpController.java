package com.avsb.pdv.controller;


import com.avsb.pdv.dto.UserDTO;
import com.avsb.pdv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/sign-up")
public class SignUpController {

    private UserService userService;

    public SignUpController(@Autowired UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity post(@Valid @RequestBody UserDTO user) {
        try {
            user.setEneabled(true);
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
