package com.bench;

public class Main {

    public static void main(String[] args) {

        String[] flavorNotes1 = {"Chocolate", "Nutty"};
        String[] flavorNotes2 = {"Citrus", "Berry"};

        Coffee coffee1 = new Coffee("Espresso", "Arabica", "Medium", 5.00, "Dark", "Colombia", false, 10, flavorNotes1, "Espresso");
        Coffee coffee2 = new Coffee("Latte", "Robusta", "Large", 4.50, "Medium", "Vietnam", true, 5, flavorNotes2, "Drip");

        System.out.println("Coffee 1: " + coffee1.getName());
        System.out.println("Price for Medium size: " + coffee1.calculatePrice("Medium"));
        System.out.println("In stock: " + coffee1.checkStock());
        coffee1.addFlavor("Caramel");
        System.out.println("Updated flavor notes: " + String.join(", ", coffee1.getFlavorNotes()));
        coffee1.updateStock(5);
        System.out.println("Updated stock: " + coffee1.getStock());
        System.out.println("Description: " + coffee1.describe());
        coffee1.discount(10);
        System.out.println("Price after discount: " + coffee1.getPrice());

        System.out.println();

        System.out.println("Coffee 2: " + coffee2.getName());
        System.out.println("Price for Large size: " + coffee2.calculatePrice("Large"));
        System.out.println("In stock: " + coffee2.checkStock());
        coffee2.addFlavor("Vanilla");
        System.out.println("Updated flavor notes: " + String.join(", ", coffee2.getFlavorNotes()));
        coffee2.updateStock(10);
        System.out.println("Updated stock: " + coffee2.getStock());
        System.out.println("Description: " + coffee2.describe());
        coffee2.discount(5);
        System.out.println("Price after discount: " + coffee2.getPrice());
    }
}
