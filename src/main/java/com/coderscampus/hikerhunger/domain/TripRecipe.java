package com.coderscampus.hikerhunger.domain;

import jakarta.persistence.*;

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

    private Integer recipeQuantity;

    private Integer recipeServings;

    private Float totalWeight;

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

    public Float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public TripRecipe(){
    }

//    @Override
//    public String toString() {
//        return "TripRecipe{" +
//                "tripRecipeId=" + tripRecipeId +
//                ", trip=" + trip +
//                ", recipe=" + recipe +
//                ", recipeQuantity=" + recipeQuantity +
//                ", recipeServings=" + recipeServings +
//                ", totalWeight=" + totalWeight +
//                '}';
//    }

    //    public TripRecipe(Long tripRecipeId, Trip trip, Recipe recipe, Integer recipeQuantity, Integer recipeServings, Float totalWeight) {
//        this.tripRecipeId = tripRecipeId;
//        this.trip = trip;
//        this.recipe = recipe;
//        this.recipeQuantity = recipeQuantity;
//        this.recipeServings = recipeServings;
//        this.totalWeight = totalWeight;
//    }
}