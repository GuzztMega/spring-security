package com.guzzmega.springsecurity.domain.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
