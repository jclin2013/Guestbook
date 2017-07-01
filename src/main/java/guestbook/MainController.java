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

  @PostMapping(path="/showAllUsers/update")
  public String updateUser(
		@RequestParam String tablename,
		@RequestParam String id,
		@RequestParam String newvalue,
		@RequestParam String colname,
		@RequestParam String coltype
	) {

		System.out.println("POST /showAllUsers/update");
		System.out.println(tablename);
		System.out.println(id);
		System.out.println(newvalue);
		System.out.println(colname);
		System.out.println(coltype);

		User n = userRepository.findOne(Integer.parseInt(id));

		if (colname.equals("name")) {
			n.setName(newvalue);
		} else if (colname.equals("email")) {
			n.setEmail(newvalue);
		} else if (colname.equals("phoneNumber")) {
			n.setPhoneNumber(newvalue);
		} //else if (colname.equals)

		userRepository.save(n);

		return "showAllUsers";
  }

	@DeleteMapping(path="/showAllUsers/delete")
	public String deleteUser(@RequestParam String id) {

		User n = userRepository.findOne(Integer.parseInt(id));
		userRepository.delete(n);

		return "showAllUsers";
  }

	@PostMapping(path="/showAllUsers/add")
	public String addUser(@RequestParam String name
      , @RequestParam String email, @RequestParam String phoneNumber) {

    User n = new User();
    n.setName(name);
    n.setEmail(email);
    n.setPhoneNumber(phoneNumber);
    userRepository.save(n);

    return "showAllUsers";
  }
	//
	// @GetMapping("/emailTargets")
	// public String emailTargets(Model model) {
	// 		model.addAttribute("user", new User());
	// 		return "user";
	// }

}
