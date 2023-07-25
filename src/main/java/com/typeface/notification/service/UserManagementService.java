package com.typeface.notification.service;

import com.typeface.notification.entity.User;
import com.typeface.notification.model.UserRequestDetail;
import com.typeface.notification.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private IUserRepository userRepository;

    public boolean validate(UserRequestDetail userRequestDetail) {
        Optional<User> users = userRepository.findById(userRequestDetail.getUserID());
        return users.isPresent();
    }

}
