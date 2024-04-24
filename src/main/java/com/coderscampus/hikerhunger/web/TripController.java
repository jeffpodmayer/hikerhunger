package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.*;
import com.coderscampus.hikerhunger.service.RecipeService;
import com.coderscampus.hikerhunger.service.TripRecipeService;
import com.coderscampus.hikerhunger.service.TripService;
import com.coderscampus.hikerhunger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class TripController {

    private UserService userService;
    private TripService tripService;
    private RecipeService recipeService;
    private TripRecipeService tripRecipeService;

    @Autowired
    public TripController(UserService userService, TripService tripService, RecipeService recipeService, TripRecipeService tripRecipeService) {
        this.userService = userService;
        this.tripService = tripService;
        this.recipeService = recipeService;
        this.tripRecipeService = tripRecipeService;
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

                Trip savedTrip = tripService.saveTrip(trip);
                System.out.println("Sent from createTrip page:" + savedTrip);

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
    public String postDeleteTrip(@PathVariable Long tripId){
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
    public ResponseEntity<Recipe> saveRecipeToTrip(@PathVariable Long recipeId, @PathVariable Long tripId){
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

        if (optionalTrip.isPresent() && optionalRecipe.isPresent()) {
                Trip trip = optionalTrip.get();
                Recipe recipe = optionalRecipe.get();

                // Create a new TripRecipes object
                TripRecipe tripRecipe = new TripRecipe();
                tripRecipe.setTrip(trip);
                tripRecipe.setRecipe(recipe);

                trip.getTripRecipe().add(tripRecipe);
                System.out.println(trip.getTripRecipe());
                // Save the updated tripRecipe
                tripRecipeService.save(tripRecipe);

            return ResponseEntity.status(HttpStatus.CREATED).body(tripRecipe.getRecipe());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
