package com.coderscampus.hikerhunger.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TripRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripRecipeId;
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    @OneToMany(mappedBy = "tripRecipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TripIngredient> tripIngredients = new ArrayList<>();
    private Integer recipeQuantity;
    private Integer recipeServings;
    private Integer totalWeight;

    public Long getTripRecipeId() {
        return tripRecipeId;
    }

    public void setTripRecipeId(Long tripRecipeId) {
        this.tripRecipeId = tripRecipeId;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<TripIngredient> getTripIngredients() {
        return tripIngredients;
    }

    public void setTripIngredients(List<TripIngredient> tripIngredients) {
        this.tripIngredients = tripIngredients;
    }

    public Integer getRecipeQuantity() {
        return recipeQuantity;
    }

    public void setRecipeQuantity(Integer recipeQuantity) {
        this.recipeQuantity = recipeQuantity;
    }

    public Integer getRecipeServings() {
        return recipeServings;
    }

    public void setRecipeServings(Integer recipeServings) {
        this.recipeServings = recipeServings;
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

}
