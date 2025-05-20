package com.btamayo.CoffeeTester.models;

import jakarta.validation.constraints.*;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Coffee {

    private int id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Size is required")
    private String size;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    private Double price;

    @NotBlank(message = "Roast level is required")
    private String roastLevel;

    @NotBlank(message = "Origin is required")
    private String origin;

    private boolean decaf;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be 0 or more")
    private Integer stock;

    @NotBlank(message = "Brew method is required")
    private String brewMethod;

    private String coffeePicture;

    // Flavor notes stored as CSV string in service, but model as List<String> for UI convenience
    private String flavorNotes = "";

    public Coffee() {
    }

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRoastLevel() {
        return roastLevel;
    }

    public void setRoastLevel(String roastLevel) {
        this.roastLevel = roastLevel;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public boolean isDecaf() {
        return decaf;
    }

    public void setDecaf(boolean decaf) {
        this.decaf = decaf;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getBrewMethod() {
        return brewMethod;
    }

    public void setBrewMethod(String brewMethod) {
        this.brewMethod = brewMethod;
    }

    public String getCoffeePicture() {
        return coffeePicture;
    }

    public void setCoffeePicture(String coffeePicture) {
        this.coffeePicture = coffeePicture;
    }

    public String getFlavorNotes() {
        return flavorNotes;
    }

    public void setFlavorNotes(String flavorNotes) {
        this.flavorNotes = flavorNotes;
    }

    // If using JPA; otherwise just note this won't persist
    private List<String> flavorNotesList = new ArrayList<>();

    public List<String> getFlavorNotesList() {
        return flavorNotesList;
    }

    public void setFlavorNotesList(List<String> flavorNotesList) {
        this.flavorNotesList = flavorNotesList;
        this.flavorNotes = String.join(",", flavorNotesList);  // sync string version
    }

}
