package com.btamayo.CoffeeTester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class CoffeeController {

    @Autowired
    private CoffeeService coffeeService;

    // Home page - displays all coffees
    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("coffees", coffeeService.getCoffeeExamList());
        return "index"; // make sure this is your main HTML page
    }

    // 🔍 Search handler
    @GetMapping("/search")
    public String searchCoffee(@RequestParam("keyword") String keyword, Model model) {
        List<CoffeeExam> result = coffeeService.searchCoffee(keyword);
        model.addAttribute("coffees", result);
        return "index";
    }

    // ➕ Show add coffee form
    @GetMapping("/add")
    public String addCoffeeForm(Model model) {
        model.addAttribute("newCoffee", new CoffeeExam());
        return "new"; // this should be your add form page (new.html)
    }

    // 💾 Save new coffee
    @PostMapping("/save")
    public String saveCoffee(@RequestParam String name,
                             @RequestParam String type,
                             @RequestParam String size,
                             @RequestParam double price,
                             @RequestParam String roastLevel,
                             @RequestParam String origin,
                             @RequestParam Boolean isDecaf,
                             @RequestParam int stock,
                             @RequestParam List<String> flavorNotes,
                             @RequestParam String brewMethod) {

        CoffeeExam c = new CoffeeExam();
        c.setId(coffeeService.getId() + 1);
        c.setName(name);
        c.setType(type);
        c.setSize(size);
        c.setPrice(price);
        c.setRoastLevel(roastLevel);
        c.setOrigin(origin);
        c.setDecaf(isDecaf);
        c.setStock(stock);
        c.setFlavorNotes(flavorNotes);
        c.setBrewMethod(brewMethod);

        coffeeService.addCoffee(c);
        return "redirect:/";
    }

    // 🗑️ Delete coffee
    @GetMapping("/delete")
    public String deleteCoffee(@RequestParam int id) {
        coffeeService.deleteCoffeeExam(id);
        return "redirect:/";
    }

    // ✏️ Show edit form
    @GetMapping("/edit")
    public String editCoffee(@RequestParam int id, Model model) {
        CoffeeExam c = coffeeService.getCoffee(id);
        if (c != null) {
            model.addAttribute("coffee", c);
            return "edit"; // edit form page (edit.html)
        }
        return "redirect:/";
    }

    // 🔁 Update existing coffee
    @PostMapping("/update")
    public String updateCoffee(@RequestParam int id,
                               @RequestParam String name,
                               @RequestParam String type,
                               @RequestParam String size,
                               @RequestParam double price,
                               @RequestParam String roastLevel,
                               @RequestParam String origin,
                               @RequestParam(required = false) Boolean isDecaf,
                               @RequestParam int stock,
                               @RequestParam String flavorNotes,
                               @RequestParam String brewMethod) {

        CoffeeExam c = coffeeService.getCoffee(id);
        if (c != null) {
            c.setName(name);
            c.setType(type);
            c.setSize(size);
            c.setPrice(price);
            c.setRoastLevel(roastLevel);
            c.setOrigin(origin);
            c.setDecaf(isDecaf != null && isDecaf);
            c.setStock(stock);
            if (flavorNotes != null && !flavorNotes.isEmpty()) {
                c.setFlavorNotes(Arrays.asList(flavorNotes.split(";")));
            }
            c.setBrewMethod(brewMethod);

            coffeeService.updateCoffee(id, c);
        }
        return "redirect:/";
    }
}
