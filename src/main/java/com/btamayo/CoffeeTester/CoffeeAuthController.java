package com.btamayo.CoffeeTester;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CoffeeAuthController {

    @Autowired
    CoffeeUserService coffeeUserService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("coffeeUser", new CoffeeUser());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("coffeeUser") @Valid CoffeeUser coffeeUser, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        CoffeeUser foundUser = coffeeUserService.findByUsername(coffeeUser.getUsername());
        if (foundUser != null && new BCryptPasswordEncoder().matches(coffeeUser.getPassword(), foundUser.getPassword())) {
            session.setAttribute("coffeeUser", foundUser);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid credentials");
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}