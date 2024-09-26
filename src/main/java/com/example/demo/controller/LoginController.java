package com.example.demo.controller;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.model.UserRoleMapEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleMapEntityRepository;

@Controller
// @RequestMapping("/public")
public class LoginController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserRoleMapEntityRepository userRoleMapEntityRepository;

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
  public ModelAndView abort(ModelAndView mav, Model model) {
    model.addAttribute(
        "errorMessageForUsercaptcha",
        "invalid credentials");

    mav.setViewName("login");
    return mav;
  }

  @RequestMapping("/logoff")
  public ModelAndView logoff(ModelAndView mav, Model model) {
    model.addAttribute(
        "message",
        "logout successfull");

    mav.setViewName("/");
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

  @PostMapping(value = "/userRegistration")
  @Transactional
  public ModelAndView saveUserRegistration(
      ModelAndView mav,
      HttpServletRequest request,
      @ModelAttribute("userRegistrationDTO") User user,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (user != null) {
      String storedCaptcha = captchaStore.get(1);
      System.out.println("yesss STORED CAPTCHA " + storedCaptcha);
      System.out.println("user.getCaptcha() " + user.getCaptcha());
      // System.out.println(storedCaptcha);
      // System.out.println(user.getCaptcha());
      if (!(storedCaptcha.equals(user.getCaptcha()))) {
        // captchaStore.remove(1); // Optional: Remove token after validation
        model.addAttribute(
            "errorMessageForUsercaptcha",
            "Captcha validation failed, please try again");
        model.addAttribute("userRegistrationDTO", user);
        mav.setViewName("userRegistration");
        return mav;
      }

      // user id validation
      if (user.getUserId() != null && !user.getUserId().isEmpty()) {
        {

          String hashPassword = passwordEncoder.encode(user.getPassword());

          user.setRealPassword(user.getPassword());
          user.setPassword(hashPassword);
          user.setUserName(user.getUserName().trim());

          User savedUser = userRepository.save(user);
          Long userCode = savedUser.getUserCode();
          UserRoleMapEntity userRoleMapEntity = new UserRoleMapEntity();
          userRoleMapEntity.setRoleId(2L);
          userRoleMapEntity.setUserCode(userCode);
          userRoleMapEntityRepository.save(userRoleMapEntity); // String message = "User details registered successfully
                                                               // with \n User Id : "
          // + savedUser.getUserId() + "\n" + "Password : " + savedUser.getRealPassword();

          mav.setViewName("redirect:" + "/registrationSuccessful");
          return mav;
        }
      } else {
        model.addAttribute("", "");
        model.addAttribute("userRegistrationDTO", new User());
        mav.setViewName("userRegistration");
        return mav;
      }
    } else {
      model.addAttribute("", "Error Occured");
      model.addAttribute("userRegistrationDTO", new User());
      mav.setViewName("userRegistration");
      return mav;
    }
  }

  @GetMapping("/registrationSuccessful")
  public String registrationSuccessful(ModelAndView mav, Model model) {
    return "registrationSuccessful";
  }

}
