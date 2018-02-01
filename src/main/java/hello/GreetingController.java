package hello;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SessionAttributes("user")
@Controller
public class GreetingController {
    @Autowired
    private UserService userService;

    @ModelAttribute
    public User intSession() {
        return new User();
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name",
            required = false, defaultValue = "World") String name, Model model) {
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
            // System.out.println(first_name + " " + last_name + " " + email + " " + password + " " + password_repeat);
            final String hash = Hashing.sha256().hashString(password,
                    StandardCharsets.UTF_8).toString();
            userService.create(new User(first_name, last_name, email, hash));
            return "redirect:/login";
        } else {
            throw new IllegalArgumentException("repeat@ che hamapatasxanum passwordin");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String Login(HttpServletResponse response) {
        response.addCookie(new Cookie("session", "test"));
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginpost(@ModelAttribute("user") User sessionuser,
                            @RequestParam("email") String email,
                            @RequestParam("password") String password,
                            @CookieValue("session") String cookie) {
        if (userService.userExistByEmail(email)) {
            User user = userService.getByEmail(email);
            String h = user.getPassword();
            password = Hashing.sha256().hashString(password,
                    StandardCharsets.UTF_8).toString();
            if (h.equals(password)) {
                sessionuser.setEmail(user.getEmail());
                sessionuser.setFirstName(user.getFirstName());
                sessionuser.setLastName(user.getLastName());
                sessionuser.setId(user.getId());
                return "redirect:/home";
            }

        } else {
            return "login";
        }
        return "login";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, @ModelAttribute User user) {
        model.addAttribute("email", user.getEmail());
        return "home";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        List<User> list1 = userService.getAllusers();
        model.addAttribute("list", list1);
        return "admin";
    }
}
