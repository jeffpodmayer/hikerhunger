package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.service.IngredientService;
import com.coderscampus.hikerhunger.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/updateIngredient/{ingredientId}")
    public ResponseEntity<Ingredient> updateIngredient(@RequestBody Ingredient updatedIngredientData, @PathVariable Long ingredientId) {
        // Find the ingredient in the database by its ID
        Optional<Ingredient> optionalIngredient = ingredientService.findById(ingredientId);

        if (optionalIngredient.isPresent()) {
            Ingredient existingIngredient = optionalIngredient.get();

            existingIngredient.setIngredientName(updatedIngredientData.getIngredientName());
            existingIngredient.setQuantity(updatedIngredientData.getQuantity());
            existingIngredient.setUnit(updatedIngredientData.getUnit());
            existingIngredient.setWeightInGrams(updatedIngredientData.getWeightInGrams());
            existingIngredient.setNotes(updatedIngredientData.getNotes());

            Ingredient updatedIngredient = ingredientService.saveIngredient(existingIngredient);

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
            ingredientService.delete(ingredient);
            return ResponseEntity.ok("Ingredient deleted!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient not Found!");
        }
    }

    }


