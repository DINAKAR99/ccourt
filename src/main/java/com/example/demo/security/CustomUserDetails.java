package com.example.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.Role;
import com.example.demo.model.ServiceMaster;
import com.example.demo.model.User;

import java.util.*;

public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(UserPrincipal.class);

    private User user;
    private List<ServiceMaster> serviceMasters;

    private Role role;

    public CustomUserDetails(User user, List<ServiceMaster> serviceMasters, Role role) {
        super();
        this.serviceMasters = serviceMasters;
        this.user = user;
        this.role = role;

        // log.info("user password username>>>>>>>>>>>>>>>>>"+user.getPassword()+ "
        // "+user.getUserId());
        // log.info("this.user password
        // username>>>>>>>>>>>>>>>>>"+this.user.getPassword()+ "
        // "+this.user.getUserId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public List<ServiceMaster> getServiceMasters() {
        return serviceMasters;
    }

    public void setServiceMasters(List<ServiceMaster> serviceMasters) {
        this.serviceMasters = serviceMasters;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
