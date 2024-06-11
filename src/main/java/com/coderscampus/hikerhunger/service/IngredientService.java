package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.TripIngredient;
import com.coderscampus.hikerhunger.repository.IngredientRepository;
import com.coderscampus.hikerhunger.repository.TripIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepo;

    private final TripIngredientRepository tripIngredientRepo;
    @Autowired
    public IngredientService(IngredientRepository ingredientRepo, TripIngredientRepository tripIngredientRepo) {
        this.ingredientRepo = ingredientRepo;
        this.tripIngredientRepo = tripIngredientRepo;
    }


    public Optional<Ingredient> findById(Long ingredientId) {
        return ingredientRepo.findById(ingredientId);
    }

    public Ingredient save(Ingredient ingredient) {
        ingredientRepo.save(ingredient);
        return ingredient;
    }

    public void delete(Ingredient ingredient) {
        List<TripIngredient> tripIngredients = tripIngredientRepo.findByIngredient(ingredient);
        tripIngredientRepo.deleteAll(tripIngredients);
        ingredientRepo.delete(ingredient);
    }

    public Ingredient createIngredient(Ingredient ingredient, Recipe recipe) {
        Ingredient newIngredient = new Ingredient();
        newIngredient.setRecipe(recipe);
        newIngredient.setIngredientName(ingredient.getIngredientName());
        newIngredient.setQuantity(ingredient.getQuantity());
        newIngredient.setUnit(ingredient.getUnit());
        newIngredient.setWeightInGrams(ingredient.getWeightInGrams());
        newIngredient.setNotes(ingredient.getNotes());
        return newIngredient;
    }

    public Ingredient updateIngredient(Ingredient updatedIngredientData, Ingredient existingIngredient) {
        existingIngredient.setIngredientName(updatedIngredientData.getIngredientName());
        existingIngredient.setQuantity(updatedIngredientData.getQuantity());
        existingIngredient.setUnit(updatedIngredientData.getUnit());
        existingIngredient.setWeightInGrams(updatedIngredientData.getWeightInGrams());
        existingIngredient.setNotes(updatedIngredientData.getNotes());
        return existingIngredient;
    }
}
