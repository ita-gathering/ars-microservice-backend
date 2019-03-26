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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Ocean Liang
 * @date 3/8/2019
 */
@Service
public class ActivityService {

    public static final String FAILED = "failed";
    @Resource
    private ActivityRepository activityRepository;
    @Resource @Qualifier("userRestTemplate")
    private RestTemplate restTemplate;
//    private static final String MIRCO_SERVICE_USER = "micro-service-user";

    public ActivityDto createActivity(Activity activity) {
        return constructActivityDto(activityRepository.save(activity));
    }

    public ActivityDto getActivityById(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) {
            return null;
        }
        return constructActivityDto(activity);
    }

    private ActivityDto constructActivityDto(Activity activity) {
        ActivityDto activityDto = WrappedBeanCopier.copyProperties(activity, ActivityDto.class);
        List<UserDto> userDtos = WrappedBeanCopier.copyPropertiesOfList(activityDto.getParticipants(), UserDto.class);
        activityDto.setParticipants(userDtos);
        return activityDto;
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

    public boolean deleteActivity(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (Objects.isNull(activity)) {
            return false;
        }
        activityRepository.delete(activity);
        return true;
    }

    public List<ActivityDto> getActivityByCriteria(AcitivitySearchCriteria searchCriteria) {
        List<Activity> activities = getActivity(searchCriteria);
        return activities.stream().map(this::constructActivityDto).collect(Collectors.toList());
    }

    private List<Activity> getActivity(AcitivitySearchCriteria searchCriteria) {
        if (searchCriteria.getAuthor() != null) {
            if (searchCriteria.getTitle() == null) {
                return activityRepository.findAllByAuthor(searchCriteria.getAuthor());
            }
            return activityRepository.findAllByTitleLikeAndAuthor(searchCriteria.getTitle(), searchCriteria.getAuthor());
        }
        if (searchCriteria.getTitle() != null) {
            return activityRepository.findAllByTitleLike(searchCriteria.getTitle());
        }
        if (searchCriteria.getParticipant() != null) {
            return activityRepository.findAllByUserName(searchCriteria.getParticipant());
        }
        return activityRepository.findAll();
    }

    public String participateActivity(String activityId, String username) {
        ResponseDto userResponseDto = restTemplate.getForObject("/user/" + username, ResponseDto.class);
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
