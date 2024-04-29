package com.coderscampus.hikerhunger.dto;

public class IngredientDTO {
    private Float quantity;
    private Float weightInGrams;

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(Float weightInGrams) {
        this.weightInGrams = weightInGrams;
    }
}
