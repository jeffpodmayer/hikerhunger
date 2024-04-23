package com.coderscampus.hikerhunger.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "trip_recipe")
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

    public Long getTripRecipeId() {
        return tripRecipeId;
    }

    public void setTripRecipeId(Long tripRecipeId) {
        this.tripRecipeId = tripRecipeId;
    }
}
