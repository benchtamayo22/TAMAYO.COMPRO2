package com.btamayo.CoffeeTester.service;

import com.btamayo.CoffeeTester.models.CoffeeUser;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CoffeeUserService {
    private List<CoffeeUser> coffeeUsers;
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        coffeeUsers = new ArrayList<>();
        passwordEncoder = new BCryptPasswordEncoder();  // Initialize BCryptPasswordEncoder

        // Dynamic path resolution (platform-independent)
        String basePath = System.getProperty("user.dir");
        File file = new File(basePath + File.separator + "Data" + File.separator + "coffee_users.csv");

        // Print absolute file path for debugging
        System.out.println("Looking for file at: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("❌ Error: coffee_users.csv file not found in the Data directory!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 2) {
                    CoffeeUser user = new CoffeeUser();
                    user.setUsername(parts[0].trim());
                    String hashedPassword = passwordEncoder.encode(parts[1].trim());  // Hash the password
                    user.setPassword(hashedPassword);
                    coffeeUsers.add(user);
                } else {
                    System.err.println("⚠️ Skipped malformed line: " + line);
                }
            }

            System.out.println("✅ Loaded " + coffeeUsers.size() + " users from CSV.");
        } catch (IOException e) {
            System.err.println("❌ Error reading coffee_users.csv: " + e.getMessage());
        }
    }

    public CoffeeUser findByUsername(String username) {
        return coffeeUsers.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public void save(CoffeeUser user) {
        coffeeUsers.add(user);
        System.out.println("✅ Saved new user: " + user.getUsername());
    }
}
