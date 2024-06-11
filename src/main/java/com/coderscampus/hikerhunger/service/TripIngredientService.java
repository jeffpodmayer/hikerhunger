package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.TripIngredient;
import com.coderscampus.hikerhunger.repository.TripIngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripIngredientService {
    private final TripIngredientRepository tripIngredientRepo;

    public TripIngredientService(TripIngredientRepository tripIngredientRepo) {
        this.tripIngredientRepo = tripIngredientRepo;
    }

    public void save(TripIngredient tripIngredient) {
        tripIngredientRepo.save(tripIngredient);
    }

    public Optional<TripIngredient> findById(Long ingredientId) {
        return tripIngredientRepo.findById(ingredientId);
    }

    public void deleteRelatedTripIngredients(Ingredient ingredient) {
        Optional<TripIngredient> optionalTripIngredient = findById(ingredient.getIngredientId());
        optionalTripIngredient.ifPresent(tripIngredientRepo::delete);
    }

    public void updateRelatedTripIngredients(Ingredient ingredient) {
        // Find Associated TripIngredient
        Optional<TripIngredient> optionalTripIngredient = findById(ingredient.getIngredientId());
        TripIngredient tripIngredient = optionalTripIngredient.get();

        // Calculate Adjustment ratio
        Integer originalRecipeServings = ingredient.getRecipe().getServings();
        Integer numOfPeople = tripIngredient.getTripRecipe().getTrip().getNumOfPeople();
        double ratio = (double) numOfPeople / originalRecipeServings;

        // Update TripIngredient's Quantity & WeightInGrams
        tripIngredient.setQuantity((float) (ingredient.getQuantity() * ratio));
        tripIngredient.setWeightInGrams((int) (ingredient.getWeightInGrams() * ratio));
        save(tripIngredient);
    }
}
