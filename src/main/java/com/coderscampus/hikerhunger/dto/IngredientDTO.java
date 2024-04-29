package com.coderscampus.hikerhunger.dto;

import com.coderscampus.hikerhunger.domain.Ingredient;

public class IngredientDTO {
    private Long ingredientId;
    private String ingredientName;
    private Float quantity;
    private String unit;
    private Float weightInGrams;
    private String notes;

    public IngredientDTO(Long ingredientId, String ingredientName, Float quantity, String unit, Float weightInGrams, String notes) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.weightInGrams = weightInGrams;
        this.notes = notes;
    }

    public IngredientDTO(Ingredient ingredient) {
        this.ingredientId = ingredient.getIngredientId();
        this.ingredientName = ingredient.getIngredientName();
        this.quantity = ingredient.getQuantity();
        this.unit = ingredient.getUnit();
        this.weightInGrams = ingredient.getWeightInGrams();
        this.notes = ingredient.getNotes();
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

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
