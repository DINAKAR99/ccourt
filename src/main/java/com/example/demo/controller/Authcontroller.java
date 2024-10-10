package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.helper.JwtHelper;
import com.example.demo.model.JwtRequest;
import com.example.demo.model.JwtResponse;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.RefreshTokenService;
import com.example.demo.security.UserLoginService;

@RestController
@RequestMapping("/public/auth")
public class Authcontroller {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        System.out.println("---+++++++++++++++++++++++++++++++");
        System.out.println(jwtRequest.getUser());

        // Perform authentication and handle failure if necessary
        ResponseEntity<?> failureResponse = this.doAuthenticate(jwtRequest.getUser(), jwtRequest.getPassword(), request,
                response);
        if (failureResponse != null) {
            return failureResponse; // Return early if authentication failed
        }

        // Authentication passed, generate token
        UserDetails userByUsername = userDetailsService.loadUserByUsername(jwtRequest.getUser());
        String token = jwtHelper.generateToken(userByUsername);

        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userByUsername.getUsername());

        // Create response object with token and refresh token
        JwtResponse jwtResponse = JwtResponse.builder()
                .jwttoken(token)
                .username(userByUsername.getUsername())
                .refreshtoken(refreshToken)
                .build();

        System.out.println(jwtResponse.toString());
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    private ResponseEntity<?> doAuthenticate(String username, String password, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
                password);

        try {
            // Attempt to authenticate the user
            authenticationManager.authenticate(authentication);

            // Load user details after successful authentication
            User user = userRepository.findByUserName(username);

            // Check if the user account is locked
            if (user != null && !user.isAccountNonLocked()) {
                long lockTimeInMillis = user.getLockTime().getTime();
                long currentTimeInMillis = System.currentTimeMillis();
                long lockTime = UserLoginService.getLockTimeDuration();

                if (lockTimeInMillis + lockTime > currentTimeInMillis) {
                    long leftLockTimeInMin = (lockTimeInMillis + lockTime - currentTimeInMillis) / (1000 * 60);
                    return handleAuthenticationFailure(
                            "Too many wrong attempts. You are locked out!. It will be unlocked after "
                                    + leftLockTimeInMin + " minutes.",
                            423);
                } else {
                    userLoginService.unlockWhenTimeExpired(user);
                    return handleAuthenticationFailure(
                            "Due to too many wrong attempts, your account was locked earlier. It is now unlocked. Please login again.",
                            423);
                }
            }

            // If no lock issue, continue
            if (user.getFailedAttempts() > 0) {
                userLoginService.resetFailedAttempts(user.getUserId());
            }

        } catch (BadCredentialsException e) {
            // Handle wrong username or password
            User user = userRepository.findByUserName(username);
            if (user == null) {
                return handleAuthenticationFailure("User does not exist", HttpServletResponse.SC_UNAUTHORIZED);
            }

            // Handle failed login attempts
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempts() < UserLoginService.MAX_FAILED_ATTEMPTS - 1) {
                    int leftLoginAttempts = UserLoginService.MAX_FAILED_ATTEMPTS - 1 - user.getFailedAttempts();
                    userLoginService.increaseFailedAttempts(user);
                    return handleAuthenticationFailure(
                            "Username or Password is incorrect. You have " + leftLoginAttempts + " chances left.",
                            HttpServletResponse.SC_UNAUTHORIZED);
                } else {

                    userLoginService.lock(user);
                    return handleAuthenticationFailure(
                            "Your account has been locked due to 3 failed attempts. It will be unlocked after 30 minutes.",
                            423);
                }
            } else if (!user.isAccountNonLocked() && userLoginService.unlockWhenTimeExpired(user)) {
                userLoginService.increaseFailedAttempts(user);
                return handleAuthenticationFailure("Username or Password is incorrect. You have "
                        + (UserLoginService.MAX_FAILED_ATTEMPTS - 1 - user.getFailedAttempts()) + " chances left.",
                        HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                long leftLockTimeInMin = (user.getLockTime().getTime() + UserLoginService.getLockTimeDuration()
                        - System.currentTimeMillis()) / (1000 * 60);
                return handleAuthenticationFailure(
                        "Too many wrong attempts. You are locked out! It will be unlocked after " + leftLockTimeInMin
                                + " minutes.",
                        423);
            }
        }

        // Return null to indicate authentication passed and proceed with token
        // generation
        return null;
    }

    private ResponseEntity<?> handleAuthenticationFailure(String message, int status) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", message);

        return ResponseEntity.status(status).body(responseBody);
    }

    @PostMapping("/create-refresh-token")
    public ResponseEntity<RefreshToken> createRefreshToken(@RequestBody String username) {
        refreshTokenService.createRefreshToken(username);
        return null;
    }

}
