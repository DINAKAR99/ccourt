package com.example.demo.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebListener
public class MyServletContextListener implements ServletContextListener {

  private static final Logger logger = LoggerFactory.getLogger(
    MyServletContextListener.class
  );

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    logger.info("...ServletContextEvent initialized.....");
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
      sce.getServletContext()
    );
    ListenerDAO daoObj = ctx.getBean(ListenerDAO.class);

    ServletContext sc = sce.getServletContext();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    logger.info("....ServletContextEvent destroyed.....");
  }
}
