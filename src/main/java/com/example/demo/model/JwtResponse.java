package com.example.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private String token;
    public String user;
    public RefreshToken refreshtoken;
}
