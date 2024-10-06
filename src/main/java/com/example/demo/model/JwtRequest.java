package com.example.demo.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtRequest {
    public String user;
    public String password;
}
