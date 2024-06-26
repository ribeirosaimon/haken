package com.example.hacken.api.controller;

import com.example.hacken.api.dto.HealthDto;
import com.example.hacken.api.service.HealthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@AllArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping
    public HealthDto health() {
        return this.healthService.apiHealthCheck();
    }


}

