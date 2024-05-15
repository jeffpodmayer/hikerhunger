package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.TripIngredient;
import com.coderscampus.hikerhunger.service.IngredientService;
import com.coderscampus.hikerhunger.service.RecipeService;
import com.coderscampus.hikerhunger.service.TripIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class IngredientController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final TripIngredientService tripIngredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService, RecipeService recipeService, TripIngredientService tripIngredientService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.tripIngredientService = tripIngredientService;
    }

      @PostMapping("/saveIngredient/{recipeId}")
      public ResponseEntity<Ingredient> saveIngredient(@RequestBody Ingredient newIngredientData, @PathVariable Long recipeId){
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            Ingredient newIngredient = ingredientService.createIngredient(newIngredientData,recipe);
            ingredientService.save(newIngredient);
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

    @PutMapping("/updateIngredient/{ingredientId}")
    public ResponseEntity<Ingredient> updateIngredient(@RequestBody Ingredient updatedIngredientData, @PathVariable Long ingredientId) {
        Optional<Ingredient> optionalIngredient = ingredientService.findById(ingredientId);
        if (optionalIngredient.isPresent()) {
                Ingredient existingIngredient = optionalIngredient.get();
                Ingredient updatedIngredient = ingredientService.updateIngredient(updatedIngredientData, existingIngredient);
                ingredientService.save(updatedIngredient);
                return ResponseEntity.ok(updatedIngredient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteIngredient/{ingredientId}")
    public ResponseEntity<String> deleteIngredient(@PathVariable Long ingredientId){
        Optional<Ingredient> optionalIngredient = ingredientService.findById(ingredientId);
        if (optionalIngredient.isPresent()) {
            Ingredient ingredient = optionalIngredient.get();
            tripIngredientService.deleteRelatedTripIngredients(ingredient);
            ingredientService.delete(ingredient);
            return ResponseEntity.ok("Ingredient deleted!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient not Found!");
        }
    }
}


