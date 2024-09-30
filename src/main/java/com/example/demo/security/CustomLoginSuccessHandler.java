package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.model.UserLoginDetails;
import com.example.demo.repository.UserLoginDetailsRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
public class CustomLoginSuccessHandler
  extends SimpleUrlAuthenticationSuccessHandler {

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Autowired
  private UserLoginService userLoginService;

  @Autowired
  private UserLoginDetailsRepository userLoginDetailsRepository;

  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication
  ) throws IOException, ServletException {
    Logger log = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    log.info(">>>>>>>>>>>>>CustomLoginSuccessHandler  >>>");
    System.out.println("the session is: " + request.getSession().getId());
    User user = userDetails.getUser();

    UserLoginDetails userLoginDetails = null, dbUserLoginDetails = null;

    if (!userDetails.getUser().isAccountNonLocked()) {
      long lockTimeInMillis = user.getLockTime().getTime();

      long currentTimeInMillis = System.currentTimeMillis();

      long lockTime = UserLoginService.getLockTimeDuration();

      if (lockTimeInMillis + lockTime > currentTimeInMillis) {
        long leftLockTimeInMin =
          ((lockTimeInMillis + lockTime) - currentTimeInMillis) / (1000 * 60);
        String msg =
          "Too many wrong attempts. You are locked out!. It will be unlocked after " +
          leftLockTimeInMin +
          " Minutes.";

        request.getSession().setAttribute("errorMessageForUsercaptcha", msg);
        redirectStrategy.sendRedirect(request, response, "/login");
      } else {
        userLoginService.unlockWhenTimeExpired(user);
        String msg =
          "Due to,too many wrong attempts your account was locked earlier,now its unlock please login again";

        request.getSession().setAttribute("errorMessageForUsercaptcha", msg);
        redirectStrategy.sendRedirect(request, response, "/login");
      }
    } else {
      dbUserLoginDetails =
        userLoginDetailsRepository.findByUserId(user.getUserId());

      if (dbUserLoginDetails == null) {
        userLoginDetails = new UserLoginDetails();

        userLoginDetails.setLoginIPAddress(request.getRemoteAddr());
        userLoginDetails.setUserId(user.getUserId());
        userLoginDetails.setUserName(user.getUserName());
        userLoginDetails.setSessionId(request.getSession().getId());
        dbUserLoginDetails = userLoginDetailsRepository.save(userLoginDetails);
      }
      if (!dbUserLoginDetails.isLogin()) {
        if (user.getFailedAttempts() > 0) {
          userLoginService.resetFailedAttempts(user.getUserId());
        }

        dbUserLoginDetails.setLogin(true);
        dbUserLoginDetails.setLoginTime(LocalDateTime.now());
        dbUserLoginDetails.setSessionId(request.getSession().getId());
        dbUserLoginDetails.setLogoutTime(null);
        dbUserLoginDetails.setLoginIPAddress(request.getRemoteAddr());
        userLoginDetailsRepository.save(dbUserLoginDetails);
        request
          .getSession()
          .setAttribute("userAgent", request.getHeader("user-agent").length());

        redirectStrategy.sendRedirect(request, response, "/protected");
      } else if (dbUserLoginDetails.isLogin()) {
        System.out.println("dual login is detected please logout first");
        redirectStrategy.sendRedirect(request, response, "/dualLogin");
      }
    }
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
