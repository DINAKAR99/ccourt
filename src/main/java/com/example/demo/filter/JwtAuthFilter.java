package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.helper.JwtHelper;

import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    public Logger l1 = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtHelper jwthelper;
    @Autowired
    private UserDetailsService userds;

    @SuppressWarnings("unused")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        l1.info("Header :{}", header);

        String userName = null;
        String token = null;

        if (header != null && header.startsWith("Bearer")) {
            token = header.substring(7);

            System.out.println(token);

            try {
                userName = jwthelper.getUsernameFromToken(token);
                System.out.println(userName);
            } catch (IllegalArgumentException e) {

                l1.info("illgal argument while fetching username");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                l1.info("Expired Jwt token");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                l1.info(" Jwt token is malformed ,some changes were made inside token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {
            l1.info("invalid header value");
        }
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userByUsername = this.userds.loadUserByUsername(userName);
            System.out.println(userByUsername);
            System.out.println("---");

            Boolean validateToken = this.jwthelper.validateToken(token, userByUsername);

            if (validateToken) {
                // set authentication

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userByUsername, validateToken, userByUsername.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                l1.info("validation fails");
            }

        }

        filterChain.doFilter(request, response);

    }

}
