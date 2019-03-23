package com.springcloud.service;

import com.springcloud.dto.ActivityDto;
import com.springcloud.dto.ResponseDto;
import com.springcloud.po.User;
import com.springcloud.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * Created by guowanyi on 2019/3/12.
 */
@Service
public class UserService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private UserRepository userRepository;

    private static final String MICRO_SERVICE_ACTIVITY = "micro-service-activity";

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User deleteUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.isNull(user)) {
            return null;
        }
        userRepository.delete(user);
        return user;
    }

    public List<ActivityDto> getActivitiesByUserName(String userName) {
        String url = "http://" + MICRO_SERVICE_ACTIVITY + "/activity?participant=" + userName;
        ResponseDto activityResponseDto = restTemplate.getForObject(url, ResponseDto.class);
        return (List<ActivityDto>) activityResponseDto.getData();
    }
}
