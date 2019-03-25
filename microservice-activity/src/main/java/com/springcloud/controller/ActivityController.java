package com.springcloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.springcloud.dto.AcitivitySearchCriteria;
import com.springcloud.dto.ActivityDto;
import com.springcloud.dto.ResponseDto;
import com.springcloud.po.Activity;
import com.springcloud.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Ocean Liang
 * @date 3/8/2019
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/activity")
public class ActivityController {
    @Resource
    private ActivityService activityService;
    @Value("${config}")
    private String config;

    @PostMapping
    public ResponseDto createActivity(@RequestBody Activity activity) {
        if (Objects.isNull(activity.getAuthor()) || Objects.isNull(activity.getTitle())
                || Objects.isNull(activity.getContent())) {
            return ResponseDto.fail("author,title,content should not be empty");
        }
        ActivityDto activityDto = activityService.createActivity(activity);
        return ResponseDto.success(activityDto);
    }

    @GetMapping("/{activityId}")
    public ResponseDto getActivityById(@PathVariable String activityId) {
        ActivityDto activityDto = activityService.getActivityById(activityId);
        if (Objects.isNull(activityDto)) {
            return ResponseDto.fail("can not find activity");
        }
        return ResponseDto.success(activityDto);
    }

    @GetMapping
    public ResponseDto getActivityByCriteria(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "participant", required = false) String participant) {
        AcitivitySearchCriteria searchCriteria = new AcitivitySearchCriteria();
        searchCriteria.setTitle(title);
        searchCriteria.setAuthor(author);
        searchCriteria.setParticipant(participant);
        return ResponseDto.success(activityService.getActivityByCriteria(searchCriteria));
    }

    @PutMapping("/{activityId}")
    public ResponseDto updateActivity(@PathVariable String activityId, @RequestBody Activity newActivity) {
        if (activityService.updateActivity(activityId, newActivity)) {
            return ResponseDto.success();
        }
        return ResponseDto.fail("update activity failed");
    }

    @DeleteMapping("/{activityId}")
    public ResponseDto deleteActivity(@PathVariable String activityId) {
        if (activityService.deleteActivity(activityId)) {
            return ResponseDto.success();
        }
        return ResponseDto.fail("delete activity failed");
    }

    @PatchMapping("/{activityId}")
    public ResponseDto participateActivity(@PathVariable String activityId, @RequestBody JSONObject jsonObject) {
        String username = (String) jsonObject.get("userName");
        if (Objects.isNull(username)) {
            return ResponseDto.fail("userName should not be empty");
        }
        String result = activityService.participateActivity(activityId, username);
        if (StringUtils.isEmpty(result)) {
            return ResponseDto.success();
        }
        return ResponseDto.fail(result);
    }

    @GetMapping("/config")
    public String testGetConfig(){
        return config;
    }
}
