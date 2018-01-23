package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
    @Autowired
    private UserService userService;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGet() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(@RequestParam("first_name") String first_name,
                               @RequestParam("last_name") String last_name,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("password_repeat") String password_repeat) {
        if (password.equals(password_repeat)) {
            System.out.println(first_name + " " + last_name + " " + email + " " + password + " " + password_repeat);
            userService.create(new User(first_name, last_name, email, password));
            return "hello";
        } else {
            throw new IllegalArgumentException("repeat@ che hamapatasxanum passwordin");
        }
    }

}
