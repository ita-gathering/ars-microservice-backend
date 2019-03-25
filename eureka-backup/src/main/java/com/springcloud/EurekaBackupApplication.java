package com.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Ocean Liang
 * @date 3/22/2019
 */

@EnableEurekaServer
@SpringBootApplication
public class EurekaBackupApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaBackupApplication.class, args);
    }
}
