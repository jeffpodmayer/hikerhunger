package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.*;
import com.coderscampus.hikerhunger.repository.TripRecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripRecipeService {

    private final TripRecipeRepository tripRecipeRepo;
    private final TripService tripService;
    private final TripIngredientService tripIngredientService;

    public TripRecipeService(TripRecipeRepository tripRecipeRepo, TripService tripService, TripIngredientService tripIngredientService) {
        this.tripRecipeRepo = tripRecipeRepo;
        this.tripService = tripService;
        this.tripIngredientService = tripIngredientService;
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
        List<Ingredient> updatedIngredients = recipe.getIngredients();
        // Update each TripRecipe with adjusted quantities and total weight
        for (TripRecipe tripRecipe : tripRecipesToUpdate) {
            // Check if the TripRecipe already contains the newly added ingredients
            for (Ingredient updatedIngredient : updatedIngredients) {
                boolean ingredientExists = tripRecipe.getTripIngredients().stream()
                        .anyMatch(tripIngredient -> tripIngredient.getIngredient().getIngredientId().equals(updatedIngredient.getIngredientId()));
                if (!ingredientExists) {
                    // Create a new TripIngredient for the added ingredient
                    TripIngredient newTripIngredient = new TripIngredient();
                    newTripIngredient.setIngredient(updatedIngredient);
                    newTripIngredient.setQuantity(updatedIngredient.getQuantity());
                    newTripIngredient.setWeightInGrams(updatedIngredient.getWeightInGrams());
                    newTripIngredient.setTripRecipe(tripRecipe);
                    tripIngredientService.save(newTripIngredient);
                    tripRecipe.getTripIngredients().add(newTripIngredient);
                }
            }

            // Update all related trip ingredients after processing all updatedIngredients
            for (Ingredient updatedIngredient : updatedIngredients) {
                tripIngredientService.updateRelatedTripIngredients(updatedIngredient);
            }
            // Calculate total weight of the TripRecipe
            int totalWeight = 0;
            for (TripIngredient tripIngredient : tripRecipe.getTripIngredients()) {
                totalWeight += tripIngredient.getWeightInGrams();
            }
            tripRecipe.setTotalWeight((float) Math.round(totalWeight));
            save(tripRecipe);

            // Update trip details
            tripService.updateTripDetails(tripRecipe.getTrip().getTripId());
        }
    }
}
