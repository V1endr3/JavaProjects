package org.example.controller;

import org.example.model.Response;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sys/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public Response queryUserInfo(@PathVariable("userId") Long userId) {
        return Response.success(userService.getUserInfo(userId));
    }

}
