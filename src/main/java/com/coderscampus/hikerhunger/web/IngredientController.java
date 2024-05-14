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
        Optional<Ingredient> optionalIngredient = ingredientService.findById(ingredientId);

        if (optionalIngredient.isPresent()) {
            Ingredient existingIngredient = optionalIngredient.get();

            existingIngredient.setIngredientName(updatedIngredientData.getIngredientName());
            existingIngredient.setQuantity(updatedIngredientData.getQuantity());
            existingIngredient.setUnit(updatedIngredientData.getUnit());
            existingIngredient.setWeightInGrams(updatedIngredientData.getWeightInGrams());
            existingIngredient.setNotes(updatedIngredientData.getNotes());

            Ingredient updatedIngredient = ingredientService.saveIngredient(existingIngredient);

            // Find Associated TripIngredient
            Optional<TripIngredient> optionalTripIngredient = tripIngredientService.findById(updatedIngredient.getIngredientId());
            System.out.println("Found trip Ingredient: " + optionalTripIngredient.toString());
            TripIngredient tripIngredient = optionalTripIngredient.get();

            // Calculate Adjustment ratio
            Integer originalRecipeServings = existingIngredient.getRecipe().getServings();
            System.out.println("Existing recipe servings: " + originalRecipeServings);
            Integer numOfPeople = tripIngredient.getTripRecipe().getTrip().getNumOfPeople();
            System.out.println("Num Of People on Trip: " + numOfPeople);
            double ratio = (double) numOfPeople / originalRecipeServings;
            System.out.println("Calculated ratio: " + ratio);

            // Update TripIngredient's Quantity & WeightInGrams
            tripIngredient.setQuantity((float) (updatedIngredient.getQuantity() * ratio));
            tripIngredient.setWeightInGrams((float) (updatedIngredient.getWeightInGrams() * ratio));
            tripIngredientService.save(tripIngredient);
            System.out.println("Updated quantity: " + tripIngredient.getQuantity());
            System.out.println("Updated weight: " + tripIngredient.getWeightInGrams());



            // Then, use the new updated TripIngredients weightInGrams to calculate totalWeightOf the TripRecipe
            // Then, use new TotalWeigh to calculate poundsPerPersonPerDay by multiplying TripRecipe weight by TripRecipe quantity and dividing  numOfPeopleOnTrip by numOfDays

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


