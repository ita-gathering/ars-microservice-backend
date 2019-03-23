package com.springcloud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.dto.AcitivitySearchCriteria;
import com.springcloud.dto.ActivityDto;
import com.springcloud.dto.ResponseDto;
import com.springcloud.dto.UserDto;
import com.springcloud.po.Activity;
import com.springcloud.po.User;
import com.springcloud.repository.ActivityRepository;
import com.springcloud.utils.WrappedBeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Ocean Liang
 * @date 3/8/2019
 */
@Service
public class ActivityService {

    public static final String FAILED = "failed";
    @Resource
    private ActivityRepository activityRepository;
    @Resource
    private RestTemplate restTemplate;
    private static final String MIRCO_SERVICE_USER = "micro-service-user";

    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity getActivityById(String activityId) {
        return activityRepository.findById(activityId).orElse(null);
    }

    public boolean updateActivity(String activityId, Activity newActivity) {
        Activity existedActivity = activityRepository.findById(activityId).orElse(null);
        if (Objects.isNull(existedActivity)) {
            return false;
        }
        existedActivity.setTitle(newActivity.getTitle());
        existedActivity.setContent(newActivity.getContent());
        existedActivity.setStartDate(newActivity.getStartDate());
        existedActivity.setClosingDate(newActivity.getClosingDate());
        activityRepository.save(existedActivity);
        return true;
    }

    public Activity deleteActivity(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (Objects.isNull(activity)) {
            return null;
        }
        activityRepository.delete(activity);
        return activity;
    }

    public List<ActivityDto> getActivityByCriteria(AcitivitySearchCriteria searchCriteria) {
        List<Activity> activities;
//        if (searchCriteria.getAuthor() != null) {
//            if (searchCriteria.getTitle() == null) {
//                activities = activityRepository.findAllByAuthor(searchCriteria.getAuthor());
//            } else {
//                activities = activityRepository.findAllByTitleLikeAndAuthor(searchCriteria.getTitle(), searchCriteria.getAuthor());
//            }
//        } else {
//            if (searchCriteria.getTitle() != null) {
//                activities = activityRepository.findAllByTitleLike(searchCriteria.getTitle());
//            } else {
//                activities = activityRepository.findAll();
//            }
//        }

        activities = activityRepository.findAllByUserName(searchCriteria.getParticipant());

        List<ActivityDto> activityDtos = WrappedBeanCopier.copyPropertiesOfList(activities, ActivityDto.class);
        activityDtos.forEach(activityDto -> {
            List<UserDto> userDtos = WrappedBeanCopier.copyPropertiesOfList(activityDto.getParticipants(), UserDto.class);
            activityDto.setParticipants(userDtos);
        });
        return activityDtos;
    }

    public String participateActivity(String activityId, String username) {
        String url = "http://" + MIRCO_SERVICE_USER + "/user/" + username;
        ResponseDto userResponseDto = restTemplate.getForObject(url, ResponseDto.class);
        if (FAILED.equals(userResponseDto.getStatus())) {
            return "can not find user";
        }
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(userResponseDto.getData(), User.class);
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (Objects.isNull(activity)) {
            return "can not find activity";
        }
        if (activity.getParticipants() != null) {
            boolean hasParticipate = activity.getParticipants().stream()
                    .anyMatch(participant -> participant.getUserName().equals(user.getUserName()));
            if (hasParticipate) {
                return "has already participate";
            }
            activity.getParticipants().add(user);
        } else {
            List<User> users = new ArrayList<>();
            users.add(user);
            activity.setParticipants(users);
        }
        activityRepository.save(activity);
        return "";
    }

}
