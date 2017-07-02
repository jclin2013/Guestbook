package guestbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import guestbook.User;
import guestbook.UserRepository;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;

import java.util.List;
// import com.querydsl.core.types.dsl.BooleanExpression;

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

		User n = userRepository.findOne(Integer.parseInt(id));

		if (colname.equals("name")) {
			n.setName(newvalue);
		} else if (colname.equals("email")) {
			n.setEmail(newvalue);
		} else if (colname.equals("phoneNumber")) {
			n.setPhoneNumber(newvalue);
		} else if (colname.equals("wantToEmail")) {
			n.setWantToEmail(Integer.parseInt(newvalue));
		}

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

  @Autowired
  private JavaMailSender sender;

	@RequestMapping("/queryTest")
	public String queryTest(Model model) {

		List<User> listOfPeople = userRepository.findAllByWantToEmail(1);

		String resultstring = "";

		for (int i = 0; i < listOfPeople.size(); i++) {
			resultstring += listOfPeople.get(i).getEmail();
		}

		model.addAttribute("resultstring", resultstring);

		return "hello";
	}

  @GetMapping(path = "/showAllUsers/sendEmail")
  @ResponseBody
  String home() {

				// List<String> users = userRepository.findByWantToEmail();
				// System.out.println(users);
				// getCheckedEmails();

        // try {
        //     sendEmail();
        //     return "Email Sent!";
        // } catch(Exception ex) {
        //     return "Error in sending email: "+ex;
        // }
				return "hi";
    }

		private void getCheckedEmails() {
			// System.out.println(userRepository.getSelectedEmails());
		}

    private void sendEmail() throws Exception{
        MimeMessage message = sender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);

				message.addRecipients(MimeMessage.RecipientType.TO,
					InternetAddress.parse("xzero.jl@gmail.com"));

        helper.setText("an email from javaguestbook");
        helper.setSubject("java guestbook");

        sender.send(message);
    }
}
