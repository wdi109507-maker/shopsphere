package com.shopsphere.model;

import com.shopsphere.exception.InsufficientStockException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Product {

    private final String sku;
    private final String name;
    private final String description;
    private final Category category;
    private BigDecimal price;
    private int stock;
    private ProductStatus status;

    public Product(String sku, String name, String description,
                   BigDecimal price, Category category, int stock) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description must not be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock must not be negative");
        }
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = normalizePrice(price);
        this.stock = stock;
        this.status = ProductStatus.ACTIVE;
    }

    private static BigDecimal normalizePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Price must not be null");
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price must not be negative");
        }
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public int getStock() {
        return stock;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return status == ProductStatus.ACTIVE && stock > 0;
    }

    public boolean hasStock(int quantity) {
        return quantity <= stock;
    }

    public void reduceStock(int quantity) {
        requirePositive(quantity);
        if (quantity > stock) {
            throw new InsufficientStockException(sku, quantity, stock);
        }
        stock -= quantity;
    }

    public void addStock(int quantity) {
        requirePositive(quantity);
        stock += quantity;
    }

    public void updatePrice(BigDecimal newPrice) {
        this.price = normalizePrice(newPrice);
    }

    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(sku, product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sku);
    }

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                '}';
    }

    private static void requirePositive(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

}