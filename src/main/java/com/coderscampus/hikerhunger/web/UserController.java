package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.Trip;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.service.RecipeService;
import com.coderscampus.hikerhunger.service.TripService;
import com.coderscampus.hikerhunger.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserServiceImpl userService;
    private final RecipeService recipeService;
    private final TripService tripService;


    @Autowired
    public UserController(UserServiceImpl uerService, RecipeService recipeService, TripService tripService) {
        this.userService = uerService;
        this.recipeService = recipeService;
        this.tripService = tripService;
    }


    @GetMapping("/home/{userId}")
    public String getUserHomepage(ModelMap model, @PathVariable Integer userId) {
        User user = userService.findUserById(userId).orElse(null);
        if(user == null){
            return "redirect:/";
        }
        List<Recipe> recipes = user.getRecipes().stream()
                .filter(recipe -> !recipe.isDeleted() && !recipeService.isRecipeEmpty(recipe))
                .collect(Collectors.toList());

        List<Trip> trips = user.getTrips().stream().filter(trip-> !tripService.isTripEmpty(trip)).collect(Collectors.toList());

        model.put("user", user);
        model.put("recipes", recipes);
        model.put("trips", trips);

        return "home";
    }

}