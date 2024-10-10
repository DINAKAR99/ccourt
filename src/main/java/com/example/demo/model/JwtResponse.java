package com.example.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private String jwttoken;
    public String username;
    public RefreshToken refreshtoken;
}
