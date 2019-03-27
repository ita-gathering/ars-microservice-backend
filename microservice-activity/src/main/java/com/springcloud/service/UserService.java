package com.springcloud.service;

import com.springcloud.dto.ResponseDto;
import com.springcloud.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "micro-service-user")
public interface UserService {
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    ResponseDto createUser(@RequestBody User user);

    @RequestMapping(value = "/user/{userName}", method = RequestMethod.GET)
    ResponseDto getUserByUserName(@PathVariable("userName") String userName);

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
    ResponseDto deleteUser(@PathVariable String userId);

}
