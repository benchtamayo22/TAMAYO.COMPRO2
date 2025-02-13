package com.bench;
// Coffee class with attributes and methods
public class Coffee {

    // Properties (Attributes)
    public String name;
    public String type;
    public String size;
    public double price;
    public String roastLevel;
    public String origin;
    public boolean isDecaf;
    public int stock;
    public String[] flavorNotes;
    public String brewMethod;

    public Coffee(String name, String type, String size, double price, String roastLevel, String origin,
                  boolean isDecaf, int stock, String[] flavorNotes, String brewMethod) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.roastLevel = roastLevel;
        this.origin = origin;
        this.isDecaf = isDecaf;
        this.stock = stock;
        this.flavorNotes = flavorNotes;
        this.brewMethod = brewMethod;
    }

    public double calculatePrice(String size) {
        switch (size) {
            case "Small":
                return price * 0.9; // 10% less for Small
            case "Medium":
                return price;
            case "Large":
                return price * 1.1; // 10% more for Large
            default:
                return price;
        }
    }

    public boolean checkStock() {
        return stock > 0;
    }

    public void addFlavor(String note) {
        String[] newFlavorNotes = new String[flavorNotes.length + 1];
        System.arraycopy(flavorNotes, 0, newFlavorNotes, 0, flavorNotes.length);
        newFlavorNotes[flavorNotes.length] = note;
        flavorNotes = newFlavorNotes;
    }

    public void updateStock(int quantity) {
        stock += quantity;
    }

    public String describe() {
        return "A " + roastLevel + " roast coffee from " + origin + " with " + String.join(", ", flavorNotes) + " notes.";
    }

    public void setDecaf(boolean isDecaf) {
        this.isDecaf = isDecaf;
    }

    public void changeRoastLevel(String newRoastLevel) {
        this.roastLevel = newRoastLevel;
    }

    public void discount(double percentage) {
        price -= price * (percentage / 100);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public String getRoastLevel() {
        return roastLevel;
    }

    public String getOrigin() {
        return origin;
    }

    public boolean isDecaf() {
        return isDecaf;
    }

    public int getStock() {
        return stock;
    }

    public String[] getFlavorNotes() {
        return flavorNotes;
    }

    public String getBrewMethod() {
        return brewMethod;
    }
}

