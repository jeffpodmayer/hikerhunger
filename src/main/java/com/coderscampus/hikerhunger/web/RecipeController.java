package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.service.RecipeService;
import com.coderscampus.hikerhunger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequestMapping("/home")
@Controller
public class RecipeController {

    private UserService userService;
    private RecipeService recipeService;

    @Autowired
    public RecipeController(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @PostMapping("/{userId}/recipe")
    public String postCreateRecipe(@PathVariable Integer userId, @ModelAttribute("recipe") Recipe recipe) {
        User user = userService.findById(userId);
        Recipe newRecipe = recipeService.createRecipe(recipe, user);
        System.out.println(newRecipe);
        return "redirect:/home/" + userId + "/recipe/" + newRecipe.getRecipeId();
    }
    @GetMapping("/{userId}/recipe/{recipeId}")
    public String getCreateRecipe(ModelMap model, @PathVariable Integer userId, @PathVariable Long recipeId) {
        User user = userService.findById(userId);
        Optional<Recipe> recipe = recipeService.findById(recipeId);
        model.put("user", user);
        model.put("recipe", recipe);
        return "create";
    }

    @PostMapping("/{userId}/recipe/{recipeId}")
    public String postCreateRecipe(@ModelAttribute Recipe recipe, @PathVariable Integer userId) {
        User user = userService.findById(userId);
        recipe.setUser(user);
        recipeService.saveRecipe(recipe);
        System.out.println("Recipe that was posted:" + recipe);
        return "redirect:/home/" + userId;
    }


}
