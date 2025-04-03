package com.btamayo.Coffee;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
public class CoffeeController {

    private final CoffeeService coffeeService;

    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }


    /**
     *
     * @param search used to search for the variable that is wanted by the user
     * @param model used to add attributes
     * @return it returns te main page of the program
     */
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String search, Model model) {
//        List<CoffeeExam> coffeeList = coffeeService.searchCoffee(search);
//        model.addAttribute("coffees", coffeeList);
        model.addAttribute("coffee", coffeeService.searchCoffee(search));

        return "index";
    }

    /**
     *
     * @param id - (int) id of the coffee
     * @return - deletes the coffee that is listed
     */
    @GetMapping("/delete")
    public String deleteCoffee(@RequestParam int id){
        coffeeService.deleteCoffeeExam(id);
        return "redirect:/";
    }

    /**
     *
     * @return - goes to the new html for the adding of new coffee
     */
    @GetMapping("/add")
    public String add(){
        return "new";
    }

    /**
     *
     * @param name (String) name of the coffee
     * @param type (String) type of the coffee
     * @param size (String) size of the coffee
     * @param price (int) price for the coffee
     * @param roastLevel (String) roast level of the coffee
     * @param origin (String) origin of the coffee
     * @param isDecaf (boolean) is it decaf or not?
     * @param stock (int) stock for the coffee
     * @param flavorNotes (String) flavor notes for the coffee
     * @param brewMethod (String) brewing method for the coffee
     * @return returns to the main page where the coffee is listed
     */
    @PostMapping("/save")
    public String save(@RequestParam String name,
                       @RequestParam String type,
                       @RequestParam String size,
                       @RequestParam double price,
                       @RequestParam String roastLevel,
                       @RequestParam String origin,
                       @RequestParam Boolean isDecaf,
                       @RequestParam int stock,
                       @RequestParam String flavorNotes,
                       @RequestParam String brewMethod){
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
        c.setFlavorNotes(Arrays.asList(flavorNotes.split(";")));
        c.setBrewMethod(brewMethod);

        coffeeService.addCoffee(c);
        return "redirect:/";
    }

    /**
     *
     * @param id - (int) id of the coffee
     * @param model - used to display the properties of the coffee
     * @return - goes to the edit.html and allows the user to edit the desired property of the coffee
     */
    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model) {
        CoffeeExam c = coffeeService.getCoffee(id);
        if(c != null){
            model.addAttribute("coffee", c);
            return "edit";
        }
        return "redirect:/";
    }

    /**
     *
     * @param id - (id) id of the coffee
     * @param name - (String) name of the coffee
     * @param type - (String) type of the coffee
     * @param size - (String) size of the coffee
     * @param price - (int) price for the coffee
     * @param roastLevel - (String) roast level of the coffee
     * @param origin - (String) origin of the coffee
     * @param isDecaf - (boolean) is it decaf or not?
     * @param stock - (int) stock for the coffee
     * @param flavorNotes - (String) flavor notes for the coffee
     * @param brewMethod - (String) brewing method for the coffee
     * @return - allows the page to recognize updates made in the edit.html and shows it in the main page after updating
     */
    @PostMapping("/update")
    public String update(@RequestParam int id,
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
        if(c != null){
            c.setName(name);
            c.setType(type);
            c.setSize(size);
            c.setPrice(price);
            c.setRoastLevel(roastLevel);
            c.setOrigin(origin);
            if(isDecaf != null){
                c.setDecaf(isDecaf);
            }
            c.setStock(stock);
            if (flavorNotes != null && !flavorNotes.isEmpty()) {
                c.setFlavorNotes(Arrays.asList(flavorNotes.split(",")));
            }
            c.setBrewMethod(brewMethod);

            coffeeService.updateCoffee(id, c);
        }
        return "redirect:/";
    }
}