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

    /**
     * Displays the home page with a list of coffees.
     *
     * @param search Search query to filter coffee entries.
     * @param model  Model object for passing data to the view.
     * @return Name of the Thymeleaf template to render.
     */
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String search, Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        model.addAttribute("coffees", coffeeService.searchCoffee(search));
        model.addAttribute("activeMenu", "home");
        return "index";
    }


    /**
     * Deletes a coffee entry by ID.
     *
     * @param id The ID of the coffee to delete.
     * @return Redirects to the home page after deletion.
     */
    @GetMapping("/delete")
    public String delete(@RequestParam int id, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        coffeeService.deleteCoffee(id);
        return "redirect:/";
    }

    /**
     * Displays the add coffee form.
     *
     * @param model Model object for passing data to the view.
     * @return Name of the Thymeleaf template to render.
     */
    @GetMapping("/add")
    public String add(Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if(currentUser == null){
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

    /**
     * Handles submission of the add coffee form.
     *
     * @param coffee         The Coffee object populated from the form.
     * @param bindingResult  Validation result.
     * @param model          Model object for passing data back to the view if there are errors.
     * @return Redirects to the home page or reloads the add form on validation failure.
     */
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

        // Assign ID BEFORE handling the image
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



    /**
     * Displays the edit form for a specific coffee entry.
     *
     * @param id    The ID of the coffee to edit.
     * @param model Model object for passing data to the view.
     * @return Name of the Thymeleaf template to render, or redirect to home if coffee not found.
     */
    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model, HttpSession session) {
        CoffeeUser currentUser = (CoffeeUser) session.getAttribute("coffeeUser");
        if(currentUser == null){
            return "redirect:/login";
        }
        Coffee coffee = coffeeService.getCoffee(id);
        if (coffee != null) {
            model.addAttribute("coffee", coffee);
            model.addAttribute("types", types);
            model.addAttribute("sizes", sizes);
            model.addAttribute("roastLevels", roastLevels);
            model.addAttribute("brewMethods", brewMethods);
            return "edit";
        }
        return "redirect:/";
    }

    /**
     * Handles submission of the edit coffee form.
     *
     * @param coffee         The updated Coffee object.
     * @param bindingResult  Validation result.
     * @param model          Model object for passing data back to the view if there are errors.
     * @return Redirects to the home page or reloads the edit form on validation failure.
     */
    @PostMapping("/update")
    public String update(@ModelAttribute("coffee") @Valid Coffee coffee,
                         BindingResult bindingResult,
                         @RequestParam(value = "flavorNotes", required = false) String[] flavorNotes,
                         HttpSession session,
                         Model model) {

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

        Coffee existing = coffeeService.getCoffee(coffee.getId());
        if (existing != null) {
            // Preserve image if not updated
            if (coffee.getCoffeePicture() == null || coffee.getCoffeePicture().isEmpty()) {
                coffee.setCoffeePicture(existing.getCoffeePicture());
            }

            // Handle flavor notes
            if (flavorNotes != null) {
                coffee.setFlavorNotes(String.join(",", flavorNotes));
            } else {
                coffee.setFlavorNotes("");
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