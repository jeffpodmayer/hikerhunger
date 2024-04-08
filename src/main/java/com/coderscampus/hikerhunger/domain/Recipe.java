package com.coderscampus.hikerhunger.domain;

import jakarta.persistence.Entity;

@Entity
public class Recipe {
    private Long recipeId;
    private String recipeName;
    private RecipeType recipeType;
    private String instructions;
    private Integer servings;
    private Float totalWeight;

    public Recipe(Long recipeId, String recipeName, RecipeType recipeType, String instructions, Integer servings, Float totalWeight) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.instructions = instructions;
        this.servings = servings;
        this.totalWeight = totalWeight;
    }

    public enum RecipeType {
        Breakfast,
        Lunch,
        Dinner,
        Snack,
        Extra
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", recipeName='" + recipeName + '\'' +
                ", recipeType=" + recipeType +
                ", instructions='" + instructions + '\'' +
                ", servings=" + servings +
                ", totalWeight=" + totalWeight +
                '}';
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Float totalWeight) {
        this.totalWeight = totalWeight;
    }
}
