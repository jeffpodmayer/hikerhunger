package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.*;
import com.coderscampus.hikerhunger.dto.IngredientDTO;
import com.coderscampus.hikerhunger.dto.RecipeDTO;
import com.coderscampus.hikerhunger.service.*;
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
    private final TripRecipeService tripRecipeService;
    private final IngredientService ingredientService;
    private final TripIngredientService tripIngredientService;

    @Autowired
    public TripController(UserService userService, TripService tripService, RecipeService recipeService, TripRecipeService tripRecipeService, IngredientService ingredientService, TripIngredientService tripIngredientService) {
        this.userService = userService;
        this.tripService = tripService;
        this.recipeService = recipeService;
        this.tripRecipeService = tripRecipeService;
        this.ingredientService = ingredientService;
        this.tripIngredientService = tripIngredientService;
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
        List<Recipe> recipes = user.getRecipes().stream()
                .filter(recipe -> !recipe.isDeleted())
                .toList();
        Optional<Trip> optionalTrip = tripService.findById(tripId);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            trip.setNumOfPeople(1);
            trip.setNumOfDays(1F);
            trip.setPoundsPerPersonPerDay(0);
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
            for(TripRecipe tripRecipe : trip.getTripRecipes()){
                tripRecipeService.delete(tripRecipe);
            }
            tripService.delete(trip);
            return "redirect:/home/" + trip.getUser().getId();
        } else {
            return "Unable to Delete Recipe";
        }
    }

    @PostMapping("/saveRecipe/{recipeId}/ToTrip/{tripId}") // CHANGE TO USE TripIngredient Entity
    public ResponseEntity<Recipe> saveRecipeToTrip(@RequestBody RecipeDTO recipeData, @PathVariable Long recipeId, @PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        Optional<TripRecipe> optionalTripRecipe = tripRecipeService.findByTripAndRecipeId(tripId, recipeId);
        Integer recipeQuantity = 1;

        if(optionalTripRecipe.isPresent()){
            TripRecipe tripRecipe = optionalTripRecipe.get();
            tripRecipe.setRecipeQuantity(tripRecipe.getRecipeQuantity() + recipeQuantity);
            tripRecipeService.save(tripRecipe);
            return ResponseEntity.status(HttpStatus.CREATED).body(tripRecipe.getRecipe());

        } else if (optionalTrip.isPresent() && optionalRecipe.isPresent()) {
            Trip trip = optionalTrip.get();
            Recipe recipe = optionalRecipe.get();

            TripRecipe tripRecipe = new TripRecipe();
            tripRecipe.setTrip(trip);
            tripRecipe.setRecipe(recipe);
            tripRecipe.setRecipeServings(recipeData.getServings());
            tripRecipe.setTotalWeight(recipeData.getTotalWeight());
            tripRecipe.setRecipeQuantity(recipeQuantity);


            List<IngredientDTO> ingredientDTOs = recipeData.getIngredients();

            for (IngredientDTO ingredientDTO : ingredientDTOs) {
                TripIngredient tripIngredient = new TripIngredient();
                tripIngredient.setTripRecipe(tripRecipe);

                Optional<Ingredient> optionalIngredient = ingredientService.findById(ingredientDTO.getIngredientId());
                Ingredient ingredient = optionalIngredient.get();
                tripIngredient.setIngredient(ingredient);
                tripIngredient.setQuantity(ingredientDTO.getQuantity());
                tripIngredient.setWeightInGrams(ingredientDTO.getWeightInGrams());

                tripRecipe.getTripIngredients().add(tripIngredient);
            }


            trip.getTripRecipes().add(tripRecipe);
            tripService.save(trip);
            System.out.println(tripRecipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(tripRecipe.getRecipe());

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deleteAllRecipes/{recipeId}/{tripId}")
    public ResponseEntity<Void> deleteAllRecipes(@PathVariable Long recipeId, @PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

        if (optionalTrip.isPresent() && optionalRecipe.isPresent()) {
            Trip trip = optionalTrip.get();
            Optional<TripRecipe> optionalTripRecipe = tripRecipeService.findByTripAndRecipeId(tripId, recipeId);

            if (optionalTripRecipe.isPresent()) {
                TripRecipe tripRecipe = optionalTripRecipe.get();
                trip.getTripRecipes().remove(tripRecipe);
                tripRecipeService.delete(tripRecipe);
                tripService.save(trip);

                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteRecipe/{recipeId}/{tripId}")
    public ResponseEntity<Void> deleteRecipeFromTrip(@PathVariable Long recipeId, @PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

        if (optionalTrip.isPresent() && optionalRecipe.isPresent()) {
            Trip trip = optionalTrip.get();
            Optional<TripRecipe> optionalTripRecipe = tripRecipeService.findByTripAndRecipeId(tripId, recipeId);

            if (optionalTripRecipe.isPresent()) {
                TripRecipe tripRecipe = optionalTripRecipe.get();

                Integer currentQuantity = tripRecipe.getRecipeQuantity();
                tripRecipe.setRecipeQuantity(currentQuantity - 1);
                tripService.save(trip);

                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/trip/{tripId}/updateRecipe/{recipeId}") // REFACTOR TO USE TripIngredient ENTITY
    public ResponseEntity<String> updateTripRecipe(@RequestBody RecipeDTO updatedRecipe, @PathVariable Long tripId, @PathVariable Long recipeId) {
        Optional<TripRecipe> optionalTripRecipe = tripRecipeService.findByTripAndRecipeId(tripId, recipeId);

        if (optionalTripRecipe.isPresent()) {
            TripRecipe tripRecipe = optionalTripRecipe.get();

            tripRecipe.setRecipeServings(updatedRecipe.getServings());
            tripRecipe.setTotalWeight(updatedRecipe.getTotalWeight());

            List<IngredientDTO> updatedIngredients = updatedRecipe.getIngredients();
            List<TripIngredient> existingTripIngredients = tripRecipe.getTripIngredients();


            for (IngredientDTO updatedIngredient : updatedIngredients) {
                Long ingredientId = updatedIngredient.getIngredientId();

                Optional<TripIngredient> optionalTripIngredient = existingTripIngredients.stream()
                        .filter(tripIngredient -> tripIngredient.getIngredient().getIngredientId().equals(ingredientId))
                        .findFirst();

                if (optionalTripIngredient.isPresent()) {
                    TripIngredient tripIngredient = optionalTripIngredient.get();
                    tripIngredient.setQuantity(updatedIngredient.getQuantity());
                    tripIngredient.setWeightInGrams(updatedIngredient.getWeightInGrams());
                    tripIngredientService.save(tripIngredient);
                }
            }
            tripRecipeService.save(tripRecipe);

            return ResponseEntity.ok("Recipe updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/fetch-trip/{tripId}")
    @ResponseBody
    public ResponseEntity<Trip> fetchTrip(@PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            System.out.println("Retrieved trip: " + trip.getTripRecipes());
            return ResponseEntity.ok().body(trip);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deleteTrip/{tripId}") //USE TRIPRECIPE ENTITY
    public ResponseEntity<Void> deleteTripIcon(@PathVariable Long tripId) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            for(TripRecipe tripRecipe : trip.getTripRecipes()){
                tripRecipeService.delete(tripRecipe);
            }
            tripService.delete(trip);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }

        @GetMapping("/edit-trip/{tripId}")
        public String getEditTrip(ModelMap model, @PathVariable Long tripId) {
            Optional<Trip> tripOptional = tripService.findById(tripId);
            if (tripOptional.isEmpty()) {
                return "redirect:/error";
            }
            Trip trip = tripOptional.get();
            User user = trip.getUser();
            List<Recipe> allRecipes = user.getRecipes();
            List<TripRecipe> tripRecipes = trip.getTripRecipes();

            // Filter recipes to include only non-deleted recipes associated with the trip
            List<Recipe> recipes = allRecipes.stream()
                    .filter(recipe -> !recipe.isDeleted() || tripRecipes.stream().anyMatch(tr -> tr.getRecipe().equals(recipe)))
                    .collect(Collectors.toList());

            model.put("user", user);
            model.put("trip", trip);
            model.put("recipes", recipes);
            model.put("tripRecipes", tripRecipes);
            return "trip/update";
        }
    }





