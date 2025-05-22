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
import java.util.*;

@Controller
public class CoffeeController {

    @Autowired
    CoffeeService coffeeService;

    private final String[] types = {"Affogato", "Espresso", "Americano", "Latte", "Cappuccino", "Mocha", "Flat White", "Iced Coffee"};
    private final String[] sizes = {"Small", "Medium", "Large"};
    private final String[] roastLevels = {"Light", "Medium", "Dark"};
    private final String[] brewMethods = {"Drip", "French Press", "Espresso", "Filter"};

    @GetMapping("/catalog")
    public String catalog(Model model) {
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
        coffees.forEach(coffee -> {
            if (coffee.getFlavorNotes() != null) {
                List<String> formatted = coffee.getFlavorNotes().stream()
                        .map(note -> note.substring(0, 1).toUpperCase() + note.substring(1))
                        .toList();
                coffee.setFlavorNotes(formatted);
            }
        });

        model.addAttribute("coffees", coffees);
        model.addAttribute("activeMenu", "home");
        return "index";
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) return "redirect:/login";

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
                        BindingResult result,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        HttpSession session,
                        Model model) {

        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) return "redirect:/login";

        if (result.hasErrors()) {
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "add";
        }

        if (!imageFile.isEmpty()) {
            String path = "data/coffee_pictures/";
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();

            String ext = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + ext;

            try {
                imageFile.transferTo(new File(dir, fileName));
                coffee.setCoffeePicture(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        coffeeService.addCoffee(coffee);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) return "redirect:/login";

        Coffee coffee = coffeeService.getCoffee(id);
        if (coffee != null) {
            model.addAttribute("coffee", coffee);
            model.addAttribute("flavorNotesString", String.join(",", coffee.getFlavorNotes()));
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "edit";
        }

        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("coffee") @Valid Coffee coffee,
                         BindingResult result,
                         @RequestParam(value = "flavorNotes", required = false) String flavorNotesString,
                         HttpSession session,
                         Model model) {

        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) return "redirect:/login";

        if (result.hasErrors()) {
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "edit";
        }

        Coffee existing = coffeeService.getCoffee(coffee.getId());
        if (existing != null) {
            if (coffee.getCoffeePicture() == null || coffee.getCoffeePicture().isEmpty()) {
                coffee.setCoffeePicture(existing.getCoffeePicture());
            }

            if (flavorNotesString != null && !flavorNotesString.isBlank()) {
                coffee.setFlavorNotes(Arrays.stream(flavorNotesString.split(","))
                        .map(String::trim)
                        .toList());
            } else {
                coffee.setFlavorNotes(new ArrayList<>());
            }

            coffeeService.updateCoffee(coffee.getId(), coffee);
        }

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if (currentUser == null) return "redirect:/login";

        coffeeService.deleteCoffee(id);
        return "redirect:/";
    }

    @GetMapping("/coffee/{id}")
    public String view(@PathVariable int id, Model model) {
        Coffee coffee = coffeeService.getCoffee(id);
        if (coffee != null) {
            model.addAttribute("coffee", coffee);
            return "coffee";
        }
        return "redirect:/";
    }
}
