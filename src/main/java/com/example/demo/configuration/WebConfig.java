package com.example.demo.configuration;

import com.example.demo.listeners.MyHttpSessionListener;
import com.example.demo.listeners.MyServletContextListener;
import com.example.demo.repository.UserLoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

  @Autowired
  private UserLoginDetailsRepository userLoginDetailsRepository;

  @Bean
  public ServletListenerRegistrationBean<MyHttpSessionListener> sessionCountListener() {
    ServletListenerRegistrationBean<MyHttpSessionListener> listenerRegBean = new ServletListenerRegistrationBean<>();
    listenerRegBean.setListener(
      new MyHttpSessionListener(userLoginDetailsRepository)
    );
    return listenerRegBean;
  }

  @Bean
  public ServletListenerRegistrationBean<MyServletContextListener> adminInfoListener() {
    ServletListenerRegistrationBean<MyServletContextListener> listenerRegBean = new ServletListenerRegistrationBean<>();
    listenerRegBean.setListener(new MyServletContextListener());
    return listenerRegBean;
  }

  @Bean(name = "messageSource")
  ResourceBundleMessageSource bundleMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8"); // Set the default encoding
    return messageSource;
  }

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }
}
