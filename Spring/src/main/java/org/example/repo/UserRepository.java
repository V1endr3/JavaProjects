package org.example.repo;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends ServiceImpl<UserMapper, User> {

}
