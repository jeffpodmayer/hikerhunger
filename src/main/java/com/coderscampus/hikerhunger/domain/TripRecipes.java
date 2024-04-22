package com.coderscampus.hikerhunger.domain;

import jakarta.persistence.Entity;

@Entity
public class TripRecipes {
    private Long tripRecipesId;
    private Long tripId;
    private Long recipeId;

    public Long getTripRecipesId() {
        return tripRecipesId;
    }

    public void setTripRecipesId(Long tripRecipesId) {
        this.tripRecipesId = tripRecipesId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}
