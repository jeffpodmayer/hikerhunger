package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.service.IngredientService;
import com.coderscampus.hikerhunger.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

      @PostMapping("/saveIngredient/{recipeId}")
      public ResponseEntity<Ingredient> saveIngredient(@RequestBody Ingredient ingredient, @PathVariable Long recipeId){
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        Ingredient newIngredient = new Ingredient();


        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            newIngredient.setRecipe(recipe);
            newIngredient.setIngredientName(ingredient.getIngredientName());
            newIngredient.setQuantity(ingredient.getQuantity());
            newIngredient.setUnit(ingredient.getUnit());
            newIngredient.setWeightInGrams(ingredient.getWeightInGrams());
            newIngredient.setNotes(ingredient.getNotes());
            ingredientService.saveIngredient(newIngredient);
            return ResponseEntity.status(HttpStatus.CREATED).body(newIngredient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/getIngredient/{ingredientId}")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable Long ingredientId) {
        Optional<Ingredient> optionalIngredient = ingredientService.findById(ingredientId);
        if(optionalIngredient.isPresent()){
            Ingredient ingredient = optionalIngredient.get();
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredient);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    }

    }

//    @PostMapping("/updateIngredient/{recipeId}")
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

