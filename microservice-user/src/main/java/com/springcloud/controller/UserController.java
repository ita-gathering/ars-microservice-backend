package com.springcloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.springcloud.dto.ResponseDto;
import com.springcloud.po.User;
import com.springcloud.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by guowanyi on 2019/3/12.
 */
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/user")
    public ResponseDto createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseDto.success(user);
    }

    @GetMapping("/user/{userName}")
    public ResponseDto getUserByUserName(@PathVariable String userName, @RequestBody JSONObject request) {
        User user = userService.getUserByUserName(userName);
        if (Objects.isNull(user)) {
            return ResponseDto.fail("can not find user");
        }
        if (user.getPassword().equals(request.get("description"))) {
            return ResponseDto.success(user);
        }
        return ResponseDto.fail("can not login, password error");
    }

    @DeleteMapping("/user/{userId}")
    public ResponseDto deleteUser(@PathVariable String userId) {
        User deletedUser = userService.deleteUser(userId);
        if (Objects.isNull(deletedUser)) {
            return ResponseDto.fail("delete user failed");
        }
        return ResponseDto.success(deletedUser);
    }

//    @GetMapping("/user/{userName}/activity")
//    public ResponseDto getUserParticipatedActivitiesByUserName(@PathVariable String userName) {
//        return ResponseDto.success(userService.getActivitiesByUserName(userName));
//    }

}
