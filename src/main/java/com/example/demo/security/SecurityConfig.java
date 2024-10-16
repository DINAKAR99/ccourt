package com.example.demo.security;

import com.example.demo.filter.CaptchaAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private CustomAuthenticationFailureHandler loginFailureHandler;

  @Autowired
  private CustomLoginSuccessHandler loginSuccessHandler;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // .csrf(csrf -> csrf
    // .ignoringAntMatchers("/api/**") // Disable CSRF for API endpoints
    // )
    http
        .csrf(csrf -> csrf.disable())
        // .headers(headers -> headers
        // .contentSecurityPolicy("default-src 'self'")
        // .and()
        // .frameOptions().sameOrigin())
        .authorizeRequests(requests -> requests
            .antMatchers("/protected").authenticated()
            .anyRequest()
            .permitAll())
        .addFilterBefore(
            new CaptchaAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class)
        .formLogin(form -> form
            .loginPage("/loginPage")
            .loginProcessingUrl("/login")
            .failureHandler(loginFailureHandler)
            .successHandler(loginSuccessHandler)
            .permitAll())
        .logout(logout -> logout
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            .logoutSuccessUrl("/logoff")
            .permitAll())
        .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler()))
        .sessionManagement(session -> session
            .sessionFixation()
            .none() // Migrate session to prevent
            // fixation attacks
            .maximumSessions(1) // Max sessions allowed
            .maxSessionsPreventsLogin(true) // Prevent new login if max sessions
            // reached
            .expiredUrl("/sessionExpired") // Redirect on session expiration
            .sessionRegistry(sessionRegistry()) // Track session registry
        );

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // @Bean
  // HttpSessionEventPublisher httpSessionEventPublisher() {
  // return new HttpSessionEventPublisher();
  // }

  // @Bean
  // InMemoryUserDetailsManager userDetailsService() throws Exception {
  // return new InMemoryUserDetailsManager(
  // User.withUsername("user")
  // .password(passwordEncoder().encode("password"))
  // .roles("USER")
  // .build(),
  // User.withUsername("admin")
  // .password(passwordEncoder().encode("admin"))
  // .roles("ADMIN")
  // .build());
  // }

  @Bean
  AccessDeniedHandler accessDeniedHandler() {
    AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
    accessDeniedHandler.setErrorPage("/accessDenied.html");
    return accessDeniedHandler;
  }

  @Bean
  SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl(); // Used for session tracking
  }

  @Bean
  HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    DefaultHttpFirewall firewall = new DefaultHttpFirewall();
    firewall.setAllowUrlEncodedSlash(true); // Allow encoded slashes in URLs
    return firewall;
  }

  // @Bean
  // public FilterRegistrationBean<XSSFilter> xssPreventFilter() {
  // FilterRegistrationBean<XSSFilter> registrationBean = new
  // FilterRegistrationBean<>();
  // registrationBean.setFilter(new XSSFilter());
  // registrationBean.addUrlPatterns("/*"); // Apply the filter to all requests
  // return registrationBean;
  // }

  @Bean
  AuthenticationProvider authProvider() {
    System.err.println("AuthenticationProvider method");
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    // provider.setPasswordEncoder(new BCryptPasswordEncoder());
    return provider;
  }
}
