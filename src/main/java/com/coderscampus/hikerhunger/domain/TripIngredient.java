package com.coderscampus.hikerhunger.domain;

import jakarta.persistence.*;

@Entity
public class TripIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripIngredientId;
    @ManyToOne
    @JoinColumn(name = "trip_recipe_id")
    private TripRecipe tripRecipe;
    @ManyToOne
    @JoinColumn(name="ingredient_id")
    private Ingredient ingredient;
    private Integer weightInGrams;
    private Float quantity;

    public Long getTripIngredientId() {
        return tripIngredientId;
    }

    public void setTripIngredientId(Long tripIngredientId) {
        this.tripIngredientId = tripIngredientId;
    }

    public TripRecipe getTripRecipe() {
        return tripRecipe;
    }

    public void setTripRecipe(TripRecipe tripRecipe) {
        this.tripRecipe = tripRecipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Integer getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(Integer weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }
}
