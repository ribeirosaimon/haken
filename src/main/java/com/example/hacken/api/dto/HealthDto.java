package com.example.hacken.api.dto;

import java.time.LocalTime;

public record HealthDto(Boolean up, LocalTime today) {
}

