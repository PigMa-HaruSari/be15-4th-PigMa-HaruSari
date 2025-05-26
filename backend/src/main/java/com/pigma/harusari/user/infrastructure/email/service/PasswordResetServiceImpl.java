package com.pigma.harusari.user.infrastructure.email.service;

import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.exception.EmailNotFoundException;
import com.pigma.harusari.user.command.exception.UserCommandErrorCode;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import com.pigma.harusari.user.infrastructure.email.exception.EmailErrorCode;
import com.pigma.harusari.user.infrastructure.email.exception.ResetEmailCodeInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    @Value("${server_port}")
    private String server_port;

    private final UserCommandRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    @Override
    public void sendResetLink(String email) {
        // 1. 사용자 검증
        Optional<Member> optional = userRepository.findByEmail(email)
                .filter(member -> Boolean.FALSE.equals(member.getUserDeletedAt()));
        if (optional.isEmpty()) {
            throw new EmailNotFoundException(UserCommandErrorCode.EMAIL_NOT_FOUND);
        }

        // 2. 랜덤 코드 생성 및 Redis 저장 (30분)
        String code = generateRandomCode();
        redisTemplate.opsForValue().set("RESET_TOKEN:" + code, email, Duration.ofMinutes(30));

        // 3. 메일 전송
        String resetLink = "http://localhost:"+server_port+"/api/v1/users/reset-password?token="+code;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[하루살이] 비밀번호 재설정 링크");
        message.setText("아래 링크를 클릭하여 비밀번호를 재설정하세요:\n" + resetLink + "\n\n유효기간: 30분");

        mailSender.send(message);
    }

    @Override
    public void verifyToken(String email, String token) {
        String storedEmail = redisTemplate.opsForValue().get("RESET_TOKEN:" + token);
        if (storedEmail == null || !storedEmail.equals(email)) {
            throw new ResetEmailCodeInvalidException(EmailErrorCode.RESET_EMAIL_CODE_INVALID);
        }
    }


    private String generateRandomCode() {
        int code = new Random().nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(code);
    }

}