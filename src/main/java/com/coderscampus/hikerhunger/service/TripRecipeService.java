package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.Trip;
import com.coderscampus.hikerhunger.domain.TripRecipe;
import com.coderscampus.hikerhunger.repository.TripRecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripRecipeService {

    private final TripRecipeRepository tripRecipeRepo;
    private final TripService tripService;

    public TripRecipeService(TripRecipeRepository tripRecipeRepo, TripService tripService) {
        this.tripRecipeRepo = tripRecipeRepo;
        this.tripService = tripService;
    }

    public void save(TripRecipe tripRecipe) {
        tripRecipeRepo.save(tripRecipe);
    }

    public Optional<TripRecipe> findByTripAndRecipeId(Long tripId, Long recipeId) {
        return tripRecipeRepo.findByTripAndRecipeId(tripId, recipeId);
    }

    public void delete(TripRecipe tripRecipe) {
        tripRecipeRepo.delete(tripRecipe);
    }

    public List<TripRecipe> findByTripId(Long tripId) {
        return tripRecipeRepo.findByTripId(tripId);
    }

    public List<TripRecipe> findByRecipeId(Long recipeId){
        return tripRecipeRepo.findByRecipeId(recipeId);
    }

    public void updateRelatedTripRecipes(Recipe recipe) {
        // Retrieve all TripRecipes associated with the updated recipe
        List<TripRecipe> tripRecipesToUpdate = findByRecipeId(recipe.getRecipeId());

        // Update each TripRecipe with adjusted quantities and total weight
        for (TripRecipe tripRecipe : tripRecipesToUpdate) {
            // Get total weight of TripRecipe by summing all weightInGrams of TripIngredients and multplying by tripRecipe quantity
            // Save the updated TripRecipe
            save(tripRecipe);
        }
    }

    private void updateTripWeightPerPersonPerDay(Trip trip) {
        // Calculate the total weight of all TripRecipes in the trip
        double totalTripWeight = trip.getTripRecipes().stream()
                .mapToDouble(tripRecipe -> tripRecipe.getTotalWeight() * tripRecipe.getRecipeQuantity() * tripRecipe.getRecipeServings())
                .sum();

        // Calculate the weight per person per day
        int numberOfPeople = trip.getNumOfPeople();
        Float tripDuration = trip.getNumOfDays();
        Float weightPerPersonPerDay = (float) (totalTripWeight  / (numberOfPeople * tripDuration));

        // Update the weightPerPersonPerDay of the trip
        trip.setPoundsPerPersonPerDay(weightPerPersonPerDay);

        // Save the updated trip
        tripService.save(trip);
    }

    private double calculateTotalWeight(Recipe recipe, int newServings) {
        double totalWeight = 0.0;
        for (Ingredient ingredient : recipe.getIngredients()) {
            double ingredientWeight = ingredient.getWeightInGrams();
            totalWeight += ingredientWeight;
        }
        return totalWeight * newServings;
    }
}
