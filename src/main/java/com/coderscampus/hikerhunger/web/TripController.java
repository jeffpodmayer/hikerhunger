package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.*;
import com.coderscampus.hikerhunger.dto.IngredientDTO;
import com.coderscampus.hikerhunger.dto.RecipeDTO;
import com.coderscampus.hikerhunger.service.RecipeService;
import com.coderscampus.hikerhunger.service.TripService;
import com.coderscampus.hikerhunger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class TripController {

    private final UserService userService;
    private final TripService tripService;
    private final RecipeService recipeService;

    @Autowired
    public TripController(UserService userService, TripService tripService, RecipeService recipeService) {
        this.userService = userService;
        this.tripService = tripService;
        this.recipeService = recipeService;
    }

    @PostMapping("/{userId}/trip")
    public String postCreateTrip(@PathVariable Integer userId, @ModelAttribute("trip") Trip trip) {
        User user = userService.findById(userId);
        Trip newTrip = tripService.createTrip(trip, user);
        return "redirect:/home/" + userId + "/trip/" + newTrip.getTripId();
    }

    @GetMapping("/{userId}/trip/{tripId}")
    public String getCreateTrip(ModelMap model, @PathVariable Integer userId, @PathVariable Long tripId) {
        User user = userService.findById(userId);
        List<Recipe> recipes = user.getRecipes();
        Optional<Trip> optionalTrip = tripService.findById(tripId);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            trip.setNumOfPeople(1);
            trip.setNumOfDays(1F);
            trip.setPoundsPerPersonPerDay(0F);
            model.put("user", user);
            model.put("recipes", recipes);
            model.put("trip", trip);
        }
        return "trip/create";
    }

    @PostMapping("/saveTrip/{tripId}")
    public String saveTrip(@ModelAttribute Trip tripData, @PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            try {
                trip.setTripName(tripData.getTripName());
                trip.setNumOfPeople(tripData.getNumOfPeople());
                trip.setNumOfDays(tripData.getNumOfDays());
                trip.setTripDetails(tripData.getTripDetails());
                trip.setPoundsPerPersonPerDay(tripData.getPoundsPerPersonPerDay());

                tripService.save(trip);
                return "redirect:/home/" + trip.getUser().getId();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("/postDeleteTrip/{tripId}")
    public String postDeleteTrip(@PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            tripService.delete(trip);
            return "redirect:/home/" + trip.getUser().getId();
        } else {
            return "Unable to Delete Recipe";
        }
    }

    @PostMapping("/saveRecipe/{recipeId}/ToTrip/{tripId}")
    public ResponseEntity<Recipe> saveRecipeToTrip(@RequestBody RecipeDTO recipeData, @PathVariable Long recipeId, @PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

        if (optionalTrip.isPresent() && optionalRecipe.isPresent()) {
            Trip trip = optionalTrip.get();
            Recipe recipe = optionalRecipe.get();
            Recipe recipeCopy = recipe.createCopy();

            recipeCopy.setServings(recipeData.getServings());
            recipeCopy.setTotalWeight(recipeData.getTotalWeight());


            List<IngredientDTO> ingredientDTOs = recipeData.getIngredients();
            List<Ingredient> ingredients = recipeCopy.getIngredients();

            for (int i = 0; i < ingredientDTOs.size(); i++) {
                IngredientDTO ingredientDTO = ingredientDTOs.get(i);
                Ingredient ingredient = ingredients.get(i);
                ingredient.setQuantity(ingredientDTO.getQuantity());
                ingredient.setWeightInGrams(ingredientDTO.getWeightInGrams());
            }

            trip.getRecipes().add(recipeCopy);
            tripService.save(trip);
            System.out.println("Updated recipe:" + recipeCopy);
            return ResponseEntity.status(HttpStatus.CREATED).body(recipeCopy);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deleteAllRecipes/{recipeId}/{tripId}")
    public ResponseEntity<Void> deleteAllRecipes(@PathVariable Long recipeId, @PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            List<Recipe> recipes = trip.getRecipes();

            recipes.removeIf(recipe -> recipe.getRecipeId().equals(recipeId));
            tripService.save(trip);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteRecipe/{recipeId}/{tripId}")
    public ResponseEntity<Void> deleteRecipeFromTrip(@PathVariable Long recipeId, @PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            List<Recipe> recipes = trip.getRecipes();
            for (Recipe recipe : recipes) {
                if (recipe.getRecipeId().equals(recipeId)) {
                    recipes.remove(recipe);
                    tripService.save(trip);
                    return ResponseEntity.noContent().build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

//    @PutMapping("/trip/{tripId}/updateRecipe/{recipeId}")
//    public void updateTripRecipe(@RequestBody Recipe updatedRecipe, @PathVariable Long tripId, @PathVariable Long recipeId) {
//        Optional<Trip> optionalTrip = tripService.findById(tripId);
//        System.out.println(optionalTrip);
//
//        if (optionalTrip.isPresent()) {
//            Trip trip = optionalTrip.get();
//            List<Recipe> recipes = trip.getRecipes();
//
//            for (Recipe existingRecipe : recipes) {
//                if (existingRecipe.getRecipeId().equals(recipeId)) {
//                    existingRecipe.setServings(updatedRecipe.getServings());
//                    existingRecipe.setTotalWeight(updatedRecipe.getTotalWeight());
//
//                    List<Ingredient> existingIngredients = existingRecipe.getIngredients();
//                    List<Ingredient> updatedIngredients = updatedRecipe.getIngredients();
//
//                    Map<Long, Ingredient> existingIngredientMap = existingIngredients.stream()
//                            .collect(Collectors.toMap(Ingredient::getIngredientId, Function.identity()));
//
//                    for (Ingredient updatedIngredient : updatedIngredients) {
//                        Long ingredientId = updatedIngredient.getIngredientId();
//                        if (existingIngredientMap.containsKey(ingredientId)) {
//                            Ingredient existingIngredient = existingIngredientMap.get(ingredientId);
//                            existingIngredient.setQuantity(updatedIngredient.getQuantity());
//                            existingIngredient.setWeightInGrams(updatedIngredient.getWeightInGrams());
//                        }
//                    }
//                System.out.println(trip.getRecipes());
//                tripService.save(trip);
//                return; // Exit the method after updating the recipe
//                }
//            }
//        }
//    }
}




