#  ars-microservice-backend

### Version
- SpringBoot：2.0.5.RELEASE
- SpringCloud：Finchley.SR2

### Start Sequence
1. EurekaApplication
2. EurekaBackupApplication
3. ConfigServerApplication
4. MicroserviceUser1Application
5. MicroserviceUser2Application
6. MicroserviceActivityApplication
7. ZuulApplication


### Url
- Eureka: http://localhost:7000/
- User: http://localhost:8181/xxx 或 http://localhost:8000/userService/xxx
- Activity: http://localhost:8282/xxx 或 http://localhost:8000/activityService/xxx
- Hystrix Dashboard: http://localhost:8282/hystrix