package com.btamayo.CoffeeTester;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CoffeeController {

    private final CoffeeService coffeeService;

    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String search, Model model) {
        model.addAttribute("coffees", coffeeService.searchCoffee(search));
        return "index";
    }

    @GetMapping("/delete")
    public String deleteCoffee(@RequestParam int id) {
        coffeeService.deleteCoffee(id);
        return "redirect:/";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("coffee", new Coffee());
        populateFormOptions(model);
        return "new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Coffee coffee) {
        coffee.setId(coffeeService.getId() + 1);
        coffeeService.addCoffee(coffee);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model) {
        Coffee c = coffeeService.getCoffee(id);
        if (c != null) {
            model.addAttribute("coffee", c);
            populateFormOptions(model);
            return "edit";
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Coffee updatedCoffee) {
        Coffee existing = coffeeService.getCoffee(updatedCoffee.getId());
        if (existing != null) {
            coffeeService.updateCoffee(updatedCoffee.getId(), updatedCoffee);
        }
        return "redirect:/";
    }

    // ✅ Helper method to populate dropdown options in model
    private void populateFormOptions(Model model) {
        model.addAttribute("types", List.of("Arabica", "Robusta"));
        model.addAttribute("sizes", List.of("Small", "Medium", "Large"));
        model.addAttribute("roastLevels", List.of("Light", "Medium", "Dark"));
        model.addAttribute("brewMethods", List.of("Drip", "French Press", "Espresso"));
    }
}
