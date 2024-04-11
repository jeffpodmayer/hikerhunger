package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.repository.IngredientRepository;
import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private IngredientRepository ingredientRepo;
    private RecipeService recipeService;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepo, RecipeService recipeService) {
        this.ingredientRepo = ingredientRepo;
        this.recipeService = recipeService;
    }

    public List<Ingredient> saveIngredients(List<Ingredient> ingredients, Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        // Check if the recipe exists
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            List<Ingredient> savedIngredients = new ArrayList<>();

            // Associate each ingredient with the recipe and save it
            for (Ingredient ingredient : ingredients) {
                ingredient.setRecipe(recipe);
                savedIngredients.add(ingredientRepo.save(ingredient));
            }
            // Return the list of saved ingredients
            return savedIngredients;
        } else {
            // Recipe with the given ID does not exist
            throw new RuntimeException("Recipe with ID " + recipeId + " not found");
        }
    }
}
