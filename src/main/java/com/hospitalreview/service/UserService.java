package com.hospitalreview.service;

import com.hospitalreview.domain.dto.UserDto;
import com.hospitalreview.domain.dto.UserJoinRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserDto join(UserJoinRequest request) {
        return new UserDto("","","");
    }
}
