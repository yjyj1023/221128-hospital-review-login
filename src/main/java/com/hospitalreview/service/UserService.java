package com.hospitalreview.service;

import com.hospitalreview.domain.User;
import com.hospitalreview.domain.dto.UserDto;
import com.hospitalreview.domain.dto.UserJoinRequest;
import com.hospitalreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest request) {
        //회원 가입 기능
        //회원 userName(id) 중복 체크
        //중복이면 회원가입 x -> 예외 발생
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user ->{
                    throw new RuntimeException("해당 UserName이 중복 됩니다.");
                });

        //회원가입(저장)
        User savedUser = userRepository.save(request.toEntity());

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }
}
