package com.example.demo.filter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.controller.LoginController;

public class CaptchaAuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        // Check if this is the login request
        if (requestURI.endsWith("/login")) {
            ConcurrentHashMap<Integer, String> capthaStore = LoginController.captchaStore;
            String captchaId = capthaStore.get(1);
            System.out.println("the uri " + requestURI);
            String t1 = (httpRequest.getParameter("captcha"));
            System.out.println("user captha: " + t1);
            System.out.println("stored captha: " + captchaId);
            String userCaptcha = httpRequest.getParameter("captcha");

            // if ((userCaptcha == null) || !(userCaptcha.equals(captchaId))) {
            // System.out.println("d]yes--------");
            // // Redirect to CAPTCHA failure page
            // // Forward the request to the CAPTCHA failure controller
            // RequestDispatcher dispatcher =
            // httpRequest.getRequestDispatcher("/captchafailure");
            // dispatcher.forward(httpRequest, httpResponse);
            // return;
            // }
        }

        // Continue filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
