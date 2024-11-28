package com.example.mysklad1;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Product {
    private String name;
    private String description;
    private int price = 0;
    private boolean inStock = false;

    public Product(String name, String description, int price, boolean inStock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.inStock = inStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}
