package com.coderscampus.hikerhunger.dto;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeDTO {
    private Long recipeId;
    private String recipeName;
    private Recipe.RecipeType recipeType;
    private String instructions;
    private Integer servings;
    private Integer totalWeight;
    private List<IngredientDTO> ingredients;

    public RecipeDTO(Long recipeId, String recipeName, Recipe.RecipeType recipeType, String instructions, Integer servings, Integer totalWeight, List<IngredientDTO> ingredients) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.instructions = instructions;
        this.servings = servings;
        this.totalWeight = totalWeight;
        this.ingredients = ingredients;
    }

    public RecipeDTO(Recipe recipe) {
        this.recipeId = recipe.getRecipeId();
        this.recipeName = recipe.getRecipeName();
        this.recipeType = recipe.getRecipeType();
        this.instructions = recipe.getInstructions();
        this.servings = recipe.getServings();
        this.totalWeight = recipe.getTotalWeight();
        this.ingredients = recipe.getIngredients().stream()
                .map(IngredientDTO::new)
                .collect(Collectors.toList());
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
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

    public Recipe.RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(Recipe.RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

}
