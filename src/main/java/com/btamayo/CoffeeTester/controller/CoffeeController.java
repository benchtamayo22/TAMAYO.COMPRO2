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
import java.util.UUID;

@Controller
public class CoffeeController {

    @Autowired
    CoffeeService coffeeService;

    private final String[] types = {"Affogato", "Espresso", "Americano", "Latte", "Cappuccino", "Mocha", "Flat White", "Iced Coffee"};
    private final String[] sizes = {"Small", "Medium", "Large"};
    private final String[] roastLevels = {"Light", "Medium", "Dark"};
    private final String[] brewMethods = {"Drip", "French Press", "Espresso", "Filter"};

    // ROOT: Redirect to login if not logged in, else home page
    @GetMapping("/")
    public String index(HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        return "redirect:/catalog";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("coffeeUser", currentUser);
        return "main";  // ✅ return the correct view name
    }


    // Catalog page - login required
    @GetMapping("/catalog")
    public String catalog(Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("coffees", coffeeService.getCoffees());
        model.addAttribute("activeMenu", "catalog");
        return "catalog";
    }

    // Add coffee form - login required
    @GetMapping("/new")
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
        model.addAttribute("activeMenu", "new");
        return "new";
    }

    // Save new coffee - login required
    @PostMapping("/save")
    public String store(@ModelAttribute("coffee") @Valid Coffee coffee,
                        BindingResult bindingResult,
                        @RequestParam("imageFile") MultipartFile coffeePicture,
                        Model model,
                        HttpSession session) {

        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "new";
        }

        if (!coffeePicture.isEmpty()) {
            String path = "data/coffee_pictures/";
            File uploadFolder = new File(path);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fileName = UUID.randomUUID() + coffeePicture.getOriginalFilename().substring(coffeePicture.getOriginalFilename().lastIndexOf("."));
            try {
                coffeePicture.transferTo(new File(uploadFolder, fileName));
                coffee.setCoffeePicture(fileName);
            } catch (IOException e) {
                System.out.println("File upload error: " + e.getMessage());
            }
        }

        coffeeService.addCoffee(coffee);  // ID set inside addCoffee()
        return "redirect:/";
    }

    // Example: Edit page (login required)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Coffee coffee = coffeeService.getCoffee(id);
        if (coffee == null) {
            return "redirect:/catalog";
        }

        model.addAttribute("coffee", coffee);
        model.addAttribute("types", types);
        model.addAttribute("sizes", sizes);
        model.addAttribute("roastLevels", roastLevels);
        model.addAttribute("brewMethods", brewMethods);
        model.addAttribute("activeMenu", "edit");
        return "edit";
    }

    // Update coffee - login required
    @PostMapping("/update/{id}")
    public String update(@PathVariable int id,
                         @ModelAttribute("coffee") @Valid Coffee coffee,
                         BindingResult bindingResult,
                         @RequestParam("imageFile") MultipartFile coffeePicture,
                         Model model,
                         HttpSession session) {

        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "edit";
        }

        if (!coffeePicture.isEmpty()) {
            String path = "data/coffee_pictures/";
            File uploadFolder = new File(path);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fileName = UUID.randomUUID() + coffeePicture.getOriginalFilename().substring(coffeePicture.getOriginalFilename().lastIndexOf("."));
            try {
                coffeePicture.transferTo(new File(uploadFolder, fileName));
                coffee.setCoffeePicture(fileName);
            } catch (IOException e) {
                System.out.println("File upload error: " + e.getMessage());
            }
        }

        coffee.setId(id);
        coffeeService.updateCoffee(id, coffee);
        return "redirect:/catalog";
    }

    // Delete coffee - login required
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        coffeeService.deleteCoffee(id);
        return "redirect:/catalog";
    }
}
