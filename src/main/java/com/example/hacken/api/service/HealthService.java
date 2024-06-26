package com.example.hacken.api.service;

import com.example.hacken.api.dto.HealthDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@AllArgsConstructor
public class HealthService {

    public HealthDto apiHealthCheck(){
        return new HealthDto(true, LocalTime.now());
    }
}
