package com.coderscampus.hikerhunger.dto;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.Trip;
import com.coderscampus.hikerhunger.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecipeDTO {
    @JsonProperty("recipeId")
    private Long recipeId;
    @JsonProperty("recipeName")
    private String recipeName;
    @JsonProperty("recipeType")
    private RecipeType recipeType;
    @JsonProperty("instructions")
    private String instructions;
    @JsonProperty("servings")
    private Integer servings;
    @JsonProperty("totalWeight")
    private Float totalWeight;

    public RecipeDTO(){
    }

    public enum RecipeType {
        BREAKFAST("Breakfast"),
        LUNCH("Lunch"),
        DINNER("Dinner"),
        SNACK("Snack"),
        EXTRA("Extra");
        private final String displayValue;
        private RecipeType(String displayValue) {
            this.displayValue = displayValue;
        }
        public String getDisplayValue() {
            return displayValue;
        }
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
