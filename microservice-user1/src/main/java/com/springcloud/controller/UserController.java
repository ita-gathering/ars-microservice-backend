package com.springcloud.controller;

import com.springcloud.dto.ResponseDto;
import com.springcloud.po.User;
import com.springcloud.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by guowanyi on 2019/3/12.
 */

@RestController
@RefreshScope
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Value("${config}")
    private String config;

    @PostMapping
    public ResponseDto createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseDto.success(user);
    }

    @GetMapping("/{userName}")
    public ResponseDto getUserByUserName(@PathVariable String userName) {
        User user = userService.getUserByUserName(userName);
        if (Objects.isNull(user)) {
            return ResponseDto.fail("can not find user");
        }
        return ResponseDto.success(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseDto deleteUser(@PathVariable String userId) {
        User deletedUser = userService.deleteUser(userId);
        if (Objects.isNull(deletedUser)) {
            return ResponseDto.fail("delete user failed");
        }
        return ResponseDto.success(deletedUser);
    }

    @GetMapping("/config")
    public String testGetConfig() {
        return config;
    }
}
