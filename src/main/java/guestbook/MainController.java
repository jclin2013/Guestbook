package guestbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;

import guestbook.User;
import guestbook.UserRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

@Controller
public class MainController {
	@Autowired
	private UserRepository userRepository;

  @GetMapping("/")
  public String guestbookForm(Model model) {
      model.addAttribute("user", new User());
      return "user";
  }

  @PostMapping("/")
  public String userSubmit(@ModelAttribute User user, @RequestParam String name
      , @RequestParam String email, @RequestParam String phoneNumber) {

    User n = new User();
    n.setName(name);
    n.setEmail(email);
    n.setPhoneNumber(phoneNumber);
    userRepository.save(n);

    return "result";
  }

  @GetMapping(path="/getAllUsers")
  public @ResponseBody Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  @GetMapping(path="/showAllUsers")
  public String showAllUsers(@ModelAttribute User user) {
    return "showAllUsers";
  }
}
