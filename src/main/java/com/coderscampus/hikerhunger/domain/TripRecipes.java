package com.coderscampus.hikerhunger.domain;

import jakarta.persistence.*;

@Entity
public class TripRecipes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripRecipesId;

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

    public Long getTripRecipesId() {
        return tripRecipesId;
    }

    public void setTripRecipesId(Long tripRecipesId) {
        this.tripRecipesId = tripRecipesId;
    }


}
