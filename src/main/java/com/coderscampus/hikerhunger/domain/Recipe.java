package com.coderscampus.hikerhunger.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;


import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "recipeId")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String recipeName;
    private RecipeType recipeType;
    private String instructions;
    private Integer servings;
    private Float totalWeight;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("recipe")
    private List<Ingredient> ingredients = new ArrayList<>();

    @ManyToMany(mappedBy = "recipes", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Trip> trips = new ArrayList<>();

    public Recipe createCopy() {
        Recipe copy = new Recipe();
        copy.setRecipeId(this.recipeId);
        copy.setUser(this.user);
        copy.setRecipeName(this.recipeName);
        copy.setRecipeType(this.recipeType);
        copy.setInstructions(this.instructions);
        copy.setServings(this.servings);
        copy.setTotalWeight(this.totalWeight);
        copy.setTrips(this.trips);


        List<Ingredient> ingredientCopies = new ArrayList<>();
        for (Ingredient ingredient : this.ingredients) {
            Ingredient ingredientCopy = new Ingredient();
            ingredientCopy.setIngredientId(ingredient.getIngredientId());
            ingredientCopy.setRecipe(ingredient.getRecipe());
            ingredientCopy.setIngredientName(ingredient.getIngredientName());
            ingredientCopy.setQuantity(ingredient.getQuantity());
            ingredientCopy.setUnit(ingredientCopy.getUnit());
            ingredientCopy.setWeightInGrams(ingredient.getWeightInGrams());
            ingredientCopy.setNotes(ingredient.getNotes());

            ingredientCopies.add(ingredientCopy);
        }
        copy.setIngredients(ingredientCopies);

        return copy;
    }



    public enum RecipeType {
        BREAKFAST("Breakfast"),
        LUNCH("Lunch"),
        DINNER("Dinner"),
        SNACK("Snack"),
        EXTRA("Extra");
        private final String displayValue;
        RecipeType(String displayValue) {
            this.displayValue = displayValue;
        }
        public String getDisplayValue() {
            return displayValue;
        }
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", user=" + user +
                ", recipeName='" + recipeName + '\'' +
                ", recipeType=" + recipeType +
                ", instructions='" + instructions + '\'' +
                ", servings=" + servings +
                ", totalWeight=" + totalWeight +
                ", ingredients=" + ingredients +
                ", trips=" + trips +
                '}';
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

}
