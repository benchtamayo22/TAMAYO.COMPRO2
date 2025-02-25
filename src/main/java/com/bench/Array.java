package com.bench;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Array {

    // 2D array to store coffee names and prices
    private static final Object[][] CoffeeMenu = {
            {"Espresso", 50.0},
            {"Latte", 70.0},
            {"Cappuccino", 65.0},
            {"Mocha", 80.0}
    };
    private static final double VAT_RATE = 0.12;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[] orderCount = new int[CoffeeMenu.length];
        String receipt = "\n---- Coffee Order Receipt ----\n";
        double total = 0.0;

        while (true) {
            displayMenu();

            System.out.print("Choose your coffee (1-" + CoffeeMenu.length + ", or 0 to finish): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 0) {
                    break;
                }
                if (choice < 1 || choice > CoffeeMenu.length) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                if (quantity < 1) {
                    System.out.println("Quantity must be at least 1. Please try again.");
                    continue;
                }

                orderCount[choice - 1] += quantity;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // Summarize the order
        for (int i = 0; i < CoffeeMenu.length; i++) {
            if (orderCount[i] > 0) {
                double price = (double) CoffeeMenu[i][1];
                double itemTotal = price * orderCount[i];
                total += itemTotal;
                receipt += String.format("%d x %s @ %.2f each = %.2f\n", orderCount[i], CoffeeMenu[i][0], price, itemTotal);
            }
        }

        double vat = total * VAT_RATE;
        double grandTotal = total + vat;

        receipt += "---------------------------\n";
        receipt += String.format("Subtotal: %.2f\n", total);
        receipt += String.format("VAT (12%%): %.2f\n", vat);
        receipt += String.format("Grand Total: %.2f\n", grandTotal);
        receipt += "---------------------------\n";

        System.out.println(receipt);

        saveReceiptToFile(receipt);
        scanner.close();
    }

    /**
     * Method to display the coffee menu to the user.
     */
    private static void displayMenu() {
        System.out.println("\n--- Coffee Menu ---");
        for (int i = 0; i < CoffeeMenu.length; i++) {
            System.out.println((i + 1) + ". " + CoffeeMenu[i][0] + " - " + CoffeeMenu[i][1] + " PHP");
        }
        System.out.println("0. Finish Order");
    }

    /**
     * Method to save the receipt to a file
     * @param receipt The receipt to save
     */
    private static void saveReceiptToFile(String receipt) {
        File saveDir = new File("target/receipts");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File receiptFile = new File(saveDir, "CoffeeReceipt.txt");
        try (FileWriter writer = new FileWriter(receiptFile)) {
            writer.write(receipt);
            System.out.println("\nReceipt saved to CoffeeReceipt.txt");
        } catch (IOException e) {
            System.out.println("Error saving receipt: " + e.getMessage());
        }
    }
}