package com.springcloud.service;

import com.springcloud.po.User;
import com.springcloud.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by guowanyi on 2019/3/12.
 */
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByUserName(String userName) {
        System.out.println("user2 has been called");
        return userRepository.findByUserName(userName);
    }

    public User deleteUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.isNull(user)) {
            return null;
        }
        userRepository.delete(user);
        return user;
    }

}
