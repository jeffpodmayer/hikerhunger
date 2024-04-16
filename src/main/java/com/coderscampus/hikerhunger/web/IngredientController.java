package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.service.IngredientService;
import com.coderscampus.hikerhunger.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class IngredientController {

    private IngredientService ingredientService;

    private RecipeService recipeService;

    @Autowired
    public IngredientController(IngredientService ingredientService, RecipeService recipeService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
    }

//    @PostMapping("/updateIngredients/{recipeId}")
//    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipeData, @PathVariable Long recipeId) {
//        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
//
//        if (optionalRecipe.isPresent()) {
//            Recipe recipe = optionalRecipe.get();
//            try {
//                // Get existing ingredients associated with the recipe
//                List<Ingredient> existingIngredients = recipe.getIngredients();
//
//                // Update existing ingredient or add new one
//                for (Ingredient ingredient : recipeData.getIngredients()) {
//                    if (ingredient.getIngredientId() != null) {
//                        // If the ingredient has an ID, it's an existing ingredient
//                        // Find the corresponding existing ingredient and update its properties
//                        Optional<Ingredient> optionalExistingIngredient = existingIngredients.stream()
//                                .filter(i -> i.getIngredientId().equals(ingredient.getIngredientId()))
//                                .findFirst();
//                        if (optionalExistingIngredient.isPresent()) {
//                            Ingredient existingIngredient = optionalExistingIngredient.get();
//                            existingIngredient.setIngredientName((ingredient.getIngredientName()));
//                            existingIngredient.setQuantity(ingredient.getQuantity());
//                            existingIngredient.setUnit(ingredient.getUnit());
//                            existingIngredient.setWeightInGrams(ingredient.getWeightInGrams());
//                            existingIngredient.setNotes(ingredient.getNotes());
//                        }
//                    } else {
//                        // If the ingredient doesn't have an ID, it's a new ingredient
//                        // Set the recipe for the new ingredient and add it to the list of existing ingredients
//                        ingredient.setRecipe(recipe);
//                        existingIngredients.add(ingredient);
//                    }
//                }
//
//                // Save the updated recipe
//                Recipe savedRecipe = recipeService.saveRecipe(recipe);
//                System.out.println("Updated Recipe: " + savedRecipe);
//
//                return ResponseEntity.ok().body(recipe);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//            }
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
