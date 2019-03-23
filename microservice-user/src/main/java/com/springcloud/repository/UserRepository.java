package com.springcloud.repository;

import com.springcloud.po.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by guowanyi on 2019/3/12.
 */
@Repository
public interface UserRepository extends MongoRepository<User,String> {
    User findByUserName(String userName);
}
