package com.btamayo.CoffeeTester.controller;

import com.btamayo.CoffeeTester.models.Coffee;
import com.btamayo.CoffeeTester.models.CoffeeUser;
import com.btamayo.CoffeeTester.service.CoffeeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class CoffeeController {

    @Autowired
    CoffeeService coffeeService;

    private final String[] types = {"Affogato", "Espresso", "Americano", "Latte", "Cappuccino", "Mocha", "Flat White", "Iced Coffee"};
    private final String[] sizes = {"Small", "Medium", "Large"};
    private final String[] roastLevels = {"Light", "Medium", "Dark"};
    private final String[] brewMethods = {"Drip", "French Press", "Espresso", "Filter"};

    @GetMapping("/catalog")
    public String menu(Model model) {
        model.addAttribute("coffees", coffeeService.getCoffees());
        model.addAttribute("activeMenu", "catalog");
        return "catalog";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "layout/main";
    }

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String search, Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Coffee> coffees = coffeeService.searchCoffee(search);

        for (Coffee coffee : coffees) {
            if (coffee.getFlavorNotes() != null) {
                List<String> capitalizedFlavorNotes = coffee.getFlavorNotes().stream()
                        .map(f -> f.substring(0, 1).toUpperCase() + f.substring(1))
                        .collect(Collectors.toList());
                coffee.setFlavorNotes(capitalizedFlavorNotes);
            }
        }

        model.addAttribute("coffees", coffees);
        model.addAttribute("activeMenu", "home");
        return "index";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        coffeeService.deleteCoffee(id);
        return "redirect:/";
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("coffee", new Coffee());
        model.addAttribute("types", types);
        model.addAttribute("sizes", sizes);
        model.addAttribute("roastLevels", roastLevels);
        model.addAttribute("brewMethods", brewMethods);
        model.addAttribute("activeMenu", "add");
        return "add";
    }

    @PostMapping("/save")
    public String store(@ModelAttribute("coffee") @Valid Coffee coffee,
                        BindingResult bindingResult,
                        @RequestParam(value = "imageFile") MultipartFile coffeePicture, Model model, HttpSession session) {

        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "add";  // Make sure the 'add' template is loaded
        }

        coffee.setId(coffeeService.getLastId() + 1);

        // Handle image upload
        if (!coffeePicture.isEmpty()) {
            String path = "data/coffee_pictures/";
            File uploadFolder = new File(path);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fileName = UUID.randomUUID() + coffeePicture.getOriginalFilename().substring(coffeePicture.getOriginalFilename().lastIndexOf("."));
            try {
                coffeePicture.transferTo(new File(uploadFolder.getAbsolutePath() + File.separator + fileName));
                coffee.setCoffeePicture(fileName);
            } catch (IOException e) {
                System.out.println("File upload error: " + e.getMessage());
            }
        }

        coffeeService.addCoffee(coffee);  // Save the coffee with the new picture
        return "redirect:/";  // Redirect to the list page
    }

    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Coffee coffee = coffeeService.getCoffee(id);
        if (coffee != null) {
            String flavorNotesString = String.join(",", coffee.getFlavorNotes()); // Join flavor notes as a string
            model.addAttribute("coffee", coffee);
            model.addAttribute("flavorNotesString", flavorNotesString); // Pass flavor notes as a string
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "edit";
        }
        return "redirect:/"; // If coffee is not found, redirect to home page
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("coffee") @Valid Coffee coffee,
                         BindingResult bindingResult,
                         @RequestParam(value = "flavorNotes", required = false) String flavorNotesString,
                         HttpSession session,
                         Model model) {

        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Debugging
        System.out.println("Received coffee: " + coffee);
        System.out.println("flavorNotesString: " + flavorNotesString);

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "edit";
        }

        Coffee existing = coffeeService.getCoffee(coffee.getId());
        if (existing != null) {
            // Preserve image if not updated
            if (coffee.getCoffeePicture() == null || coffee.getCoffeePicture().isEmpty()) {
                coffee.setCoffeePicture(existing.getCoffeePicture());
            }

            // Handle flavor notes (comma-separated string)
            if (flavorNotesString != null && !flavorNotesString.isEmpty()) {
                coffee.setFlavorNotes(Arrays.asList(flavorNotesString.split(",")));
            } else {
                coffee.setFlavorNotes(List.of()); // Ensure empty list if no flavor notes
            }

            coffeeService.updateCoffee(coffee.getId(), coffee);
        }

        return "redirect:/";
    }

    @GetMapping("/coffee/{id}")
    public String view(@PathVariable int id, Model model) {
        Coffee coffee = coffeeService.getCoffee(id);
        model.addAttribute("coffee", coffee);
        return "coffee"; // This should point to a Thymeleaf template named coffee.html
    }
}
