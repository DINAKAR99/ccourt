package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;

@Controller
// @RequestMapping("/public")
public class LoginController {
  public static final ConcurrentHashMap<Integer, String> captchaStore = new ConcurrentHashMap<>();

  @GetMapping(value = "loginPage")
  public ModelAndView loginPage(ModelMap model) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("login");
    return mav;
  }

  @GetMapping(value = "public/forgotPassword")
  public String forgotPassword(
      HttpServletRequest request,
      Authentication authentication) {
    request.setAttribute("userIdBlock", true);

    System.out.println("forgot password >>>>>>>>>>>>>>>>>>");
    return "forgotPassword";
  }

  @ResponseBody
  @GetMapping("/captcha/number")
  public String generateCaptcha() {

    // Define the character set: digits and uppercase letters
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    SecureRandom random = new SecureRandom(); // Use SecureRandom for better randomness

    // Define the length of the CAPTCHA
    int length = 6; // Adjust length as needed

    // Generate the CAPTCHA string
    StringBuilder captcha = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int index = random.nextInt(characters.length());
      captcha.append(characters.charAt(index));
    }

    // Store the CAPTCHA string with a token (if needed)
    // String token = UUID.randomUUID().toString();
    // captchaStore.put(token, captcha.toString());
    // // Store the CAPTCHA number with the token
    captchaStore.put(1, captcha.toString());
    return captcha.toString();

  }

  @RequestMapping("/captchafailure")
  public ModelAndView captchaFailure(ModelAndView mav, Model model) {
    model.addAttribute(
        "errorMessageForUsercaptcha",
        "Captcha validation failed, please try again");

    mav.setViewName("login");
    return mav;
  }

  @RequestMapping("/abort")
  public ModelAndView loginfailed(ModelAndView mav, Model model) {
    model.addAttribute(
        "errorMessageForUsercaptcha",
        "Invalid Credentials try again");

    mav.setViewName("login");
    return mav;
  }

  @GetMapping(value = "/userRegistration")
  public ModelAndView userRegistration(ModelMap model) {
    User user = new User();
    model.addAttribute("userRegistrationDTO", user);
    ModelAndView mav = new ModelAndView();
    mav.setViewName("userRegistration");

    return mav;
  }

}
