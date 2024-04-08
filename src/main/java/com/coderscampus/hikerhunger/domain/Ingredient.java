package com.coderscampus.hikerhunger.domain;

import jakarta.persistence.Entity;

@Entity
public class Ingredient {
    private Long ingredientId;
    private String ingredientName;
    private Float quantity;
    private String unit;
    private Float weightInGrams;
    private String notes;

    public Ingredient(Long ingredientId, String ingredientName, Float quantity, String unit, Float weightInGrams, String notes) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.weightInGrams = weightInGrams;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientId=" + ingredientId +
                ", ingredientName='" + ingredientName + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", weightInGrams=" + weightInGrams +
                ", notes='" + notes + '\'' +
                '}';
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(Float weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
