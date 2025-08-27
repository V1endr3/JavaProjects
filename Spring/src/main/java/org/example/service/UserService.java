package org.example.service;

import org.example.model.User;
import org.example.repo.UserRepository;
import org.example.util.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserInfo(Long userId) {
        User userInfo = userRepository.getById(userId);

        if (userInfo == null) {
            throw AppException.create("用户不存在：%s", userId);
        }
        return userInfo;
    }

}
