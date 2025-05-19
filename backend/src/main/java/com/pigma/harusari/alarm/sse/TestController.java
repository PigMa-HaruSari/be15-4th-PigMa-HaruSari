package com.pigma.harusari.alarm.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigma.harusari.alarm.command.dto.AlarmCreateDto;
import com.pigma.harusari.alarm.command.entity.Alarm;
import com.pigma.harusari.alarm.command.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final AlarmService alarmService;
    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String sendTestMessage() {
        AlarmCreateDto dto = AlarmCreateDto.builder()
                .memberId(1L)
                .alarmMessage("DB 포함 Test Alarm")
                .type("TEST")
                .build();

        Alarm alarm = alarmService.createAlarm(dto);  // 👉 DB 저장
        rabbitTemplate.convertAndSend("alarm.exchange", "alarm.key", alarm); // 👉 MQ 전송

        return "Sent with DB!";
    }

}