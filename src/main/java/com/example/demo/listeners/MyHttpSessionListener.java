package com.example.demo.listeners;

import com.example.demo.model.UserLoginDetails;
import com.example.demo.repository.UserLoginDetailsRepository;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebListener
@Component
public class MyHttpSessionListener implements HttpSessionListener {

  public final UserLoginDetailsRepository userLoginDetailsRepository;

  public final AtomicInteger sessionCount = new AtomicInteger();

  public AtomicInteger getcount() {
    return sessionCount;
  }

  public MyHttpSessionListener(
    UserLoginDetailsRepository userLoginDetailsRepository
  ) {
    this.userLoginDetailsRepository = userLoginDetailsRepository;
  }

  @Override
  public void sessionCreated(HttpSessionEvent se) {
    if (userLoginDetailsRepository != null) {
      System.out.println("its not null --------------------------------");
    }
    System.out.println("NEW SESSION CREATED: " + se.getSession().getId());
    System.out.println("Total Active Session: " + sessionCount.get());

    se.getSession().setMaxInactiveInterval(30 * 60); // 30mins 30 * 60 seconds
    sessionCount.incrementAndGet();
    setActiveSessionCount(se);
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
    UserLoginDetails dbUserLoginDetails = null;

    if (se.getSession().getId() != null) {
      System.out.println(
        "session id in destroy  method >>>>>>>>>>>>>>>  " +
        se.getSession().getId()
      );

      System.out.println(
        "se.getSession().getMaxInactiveInterval()  :::::::::::::::::: " +
        se.getSession().getMaxInactiveInterval()
      );

      if (se.getSession().getMaxInactiveInterval() == (30 * 60)) {
        dbUserLoginDetails =
          userLoginDetailsRepository.findBySessionId(se.getSession().getId());
        if (dbUserLoginDetails != null) {
          System.out.println("<<<<<<<<<<<<<<");
          dbUserLoginDetails.setLogin(Boolean.FALSE);
          dbUserLoginDetails.setLogoutTime(LocalDateTime.now());
          dbUserLoginDetails.setSessionId("");
          userLoginDetailsRepository.save(dbUserLoginDetails);
        }
      }
    }

    System.out.println("sessionDestroyed >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    sessionCount.decrementAndGet();
    setActiveSessionCount(se);
  }

  private void setActiveSessionCount(HttpSessionEvent se) {
    se
      .getSession()
      .getServletContext()
      .setAttribute("activeSessions", sessionCount.get());
  }
}
