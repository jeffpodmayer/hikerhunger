package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.dto.RecipeDTO;
import com.coderscampus.hikerhunger.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/home")
@Controller
public class RecipeController {

    private final UserService userService;
    private final RecipeService recipeService;
    private final TripRecipeService tripRecipeService;

    @Autowired
    public RecipeController(UserService userService, RecipeService recipeService, TripRecipeService tripRecipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
        this.tripRecipeService = tripRecipeService;
    }

    @PostMapping("/{userId}/recipe")
    public String postCreateRecipe(@PathVariable Integer userId, @ModelAttribute("recipe") Recipe recipe) {
        User user = userService.findById(userId);
        Recipe newRecipe = recipeService.createRecipe(recipe, user);
        return "redirect:/home/" + userId + "/recipe/" + newRecipe.getRecipeId();
    }

    @GetMapping("/{userId}/recipe/{recipeId}") 
    public String getCreateRecipe(ModelMap model, @PathVariable Integer userId, @PathVariable Long recipeId) {
        User user = userService.findById(userId);
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipeService.setRecipeServingsAndWeight(recipe);
            List<Ingredient> ingredients = recipe.getIngredients();
            Ingredient ingredient = new Ingredient();
            model.put("user", user);
            model.put("recipe", recipe);
            model.put("ingredients", ingredients);
            model.put("ingredient", ingredient);
        }
        return "recipe/create";
    }

    @PostMapping("/saveRecipe/{recipeId}")
    public String saveRecipe(@ModelAttribute Recipe recipeData, @PathVariable Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            try {
                recipeService.updateRecipe(recipeData, recipe);
                return "redirect:/home/" + recipe.getUser().getId();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("/postDeleteRecipe/{recipeId}")
    public String postDeleteRecipe(@PathVariable Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipeService.delete(recipe);
            return "redirect:/home/" + recipe.getUser().getId();
        } else {
            return "Unable to Delete Recipe";
        }
    }

    @PostMapping("/updateRecipe/{recipeId}")
    public String updateRecipe(@ModelAttribute Recipe recipeData, @PathVariable Long recipeId) {
//        try {
            Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

            if (optionalRecipe.isPresent()) {
                Recipe recipe = optionalRecipe.get();
                recipeService.updateRecipe(recipeData, recipe);
                tripRecipeService.updateRelatedTripRecipes(recipe);

                return "redirect:/home/" + recipe.getUser().getId();
            } else {
                return "Recipe not found!";
            }
//        } catch (Exception e) {
//            return "Error updating recipe: " + e.getMessage();
//        }
    }

    @GetMapping("/edit-recipe/{recipeId}")
    public String getEditRecipe(ModelMap model, @PathVariable Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if(optionalRecipe.isPresent()){
            User user = optionalRecipe.get().getUser();
            List<Ingredient> ingredients = optionalRecipe.get().getIngredients();
            model.put("user", user);
            model.put("recipe", optionalRecipe.get());
            model.put("ingredients", ingredients);
        }
        return "recipe/update";
    }

    @GetMapping("/fetchAllRecipes")
    public ResponseEntity<List<RecipeDTO>> fetchAllRecipes() {
        List<Recipe> allRecipes = recipeService.findAll();
        List<RecipeDTO> recipeDTOs = allRecipes.stream()
                .map(RecipeDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(recipeDTOs);
    }

    @GetMapping("/fetch-recipe/{recipeId}")
    @ResponseBody
    public ResponseEntity<Recipe> fetchRecipe(@PathVariable Long recipeId) {
        Optional<Recipe> recipeOptional = recipeService.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            return ResponseEntity.ok().body(recipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deleteRecipe/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipeService.delete(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
