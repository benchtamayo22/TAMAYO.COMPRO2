package com.tamayo.projectx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class ProjectxApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectxApplication.class, args);
	}

	@GetMapping("about-me")
	public String aboutMe(Model model){
		String fullName = "Bench Tamayo";
		String favoriteQuote = "The worst part of winning is destroying your opponent's dream just to fulfill your own.";
		String descriptionAboutSelf = "I love to play badminton.";

		model.addAttribute("fullName", fullName);
		model.addAttribute("favoriteQuote", favoriteQuote);
		model.addAttribute("descriptionAboutSelf", descriptionAboutSelf);

		return "about_me";
	}
}
