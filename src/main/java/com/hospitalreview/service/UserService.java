package com.hospitalreview.service;

import com.hospitalreview.domain.User;
import com.hospitalreview.domain.dto.UserDto;
import com.hospitalreview.domain.dto.UserJoinRequest;
import com.hospitalreview.exception.ErrorCode;
import com.hospitalreview.exception.HospitalReviewAppException;
import com.hospitalreview.repository.UserRepository;
import com.hospitalreview.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimeMs = 1000 * 60 * 60; //1시간

    public UserDto join(UserJoinRequest request) {
        //회원 가입 기능
        //회원 userName(id) 중복 체크
        //중복이면 회원가입 x -> 예외 발생
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user ->{
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("UserName:%s 가 중복됩니다.", request.getUserName()));
                });

        //회원가입(저장)
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }

    public String login(String userName, String password) {

        //userName 있는지 여부 확인
        //없으면 NOT_FOUND 에러 발생
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.NOT_FOUND, String.format("%s는 가입된 적이 없습니다.", userName)));

        //password 일치하는지 여부 확인
        if(!encoder.matches(password, user.getPassword())){
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD, "userName 또는 password 가 잘못 되었습니다.");
        }

        //두가지 확인이 통과하면 토큰 발행
        return JwtTokenUtil.createToken(userName, secretKey, expireTimeMs);
    }
}
