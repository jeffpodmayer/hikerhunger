package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.service.IngredientService;
import com.coderscampus.hikerhunger.service.RecipeService;
import com.coderscampus.hikerhunger.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.spi.ToolProvider.findFirst;

@RequestMapping("/home")
@Controller
public class RecipeController {

    private UserService userService;
    private RecipeService recipeService;
    private IngredientService ingredientService;

    @Autowired
    public RecipeController(UserService userService, RecipeService recipeService, IngredientService ingredientService) {
        this.userService = userService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @PostMapping("/{userId}/recipe")
    public String postCreateRecipe(@PathVariable Integer userId, @ModelAttribute("recipe") Recipe recipe) {
        User user = userService.findById(userId);
        Recipe newRecipe = recipeService.createRecipe(recipe, user);
//        System.out.println("Created new recipe:" + newRecipe);
        return "redirect:/home/" + userId + "/recipe/" + newRecipe.getRecipeId();
    }

    @GetMapping("/{userId}/recipe/{recipeId}")
    public String getCreateRecipe(ModelMap model, @PathVariable Integer userId, @PathVariable Long recipeId) {
        User user = userService.findById(userId);
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
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
                recipe.setRecipeName(recipeData.getRecipeName());
                recipe.setRecipeType(recipeData.getRecipeType());
                recipe.setInstructions(recipeData.getInstructions());
                recipe.setServings(recipeData.getServings());
                recipe.setTotalWeight(recipeData.getTotalWeight());

                Recipe savedRecipe = recipeService.saveRecipe(recipe);
                System.out.println("Sent from createRecipe page:" + savedRecipe);

                return "redirect:/home/" + recipe.getUser().getId();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("/deleteRecipe/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipeService.delete(recipe);
            return ResponseEntity.noContent().build(); // Return 204 No Content for successful deletion
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if recipe with the given ID doesn't exist
        }
    }


    @PostMapping("/updateRecipe/{recipeId}")
    public String updateRecipe(@ModelAttribute Recipe recipeData, @PathVariable Long recipeId) {
        try {
            Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

            if (optionalRecipe.isPresent()) {
                Recipe recipe = optionalRecipe.get();
                // Update recipe information
                recipe.setRecipeName(recipeData.getRecipeName());
                recipe.setRecipeType(recipeData.getRecipeType());
                recipe.setInstructions(recipeData.getInstructions());
                recipe.setServings(recipeData.getServings());
                recipe.setTotalWeight(recipeData.getTotalWeight());

                // Save the updated recipe
                Recipe savedRecipe = recipeService.saveRecipe(recipe);
                System.out.println("Updated Recipe: " + savedRecipe);

                return "redirect: /home/" + recipe.getUser().getId();

            } else {
                // Recipe with given ID not found
                return "Recipe not found!";
            }
        } catch (Exception e) {
            // Internal server error
            return "Error updating recipe: " + e.getMessage();
        }
    }



@GetMapping("/fetch-recipe/{recipeId}")
@ResponseBody
public ResponseEntity<Recipe> fetchRecipe(@PathVariable Long recipeId) {
    Optional<Recipe> recipeOptional = recipeService.findById(recipeId);
    if (recipeOptional.isPresent()) {
        Recipe recipe = recipeOptional.get();
        System.out.println("Retrieved recipe: " + recipe);
        return ResponseEntity.ok().body(recipe);
    } else {
        return ResponseEntity.notFound().build();
    }
}

@GetMapping("/edit-recipe/{recipeId}")
public String getEditRecipe(ModelMap model, @PathVariable Long recipeId) {
    Optional<Recipe> recipe = recipeService.findById(recipeId);
    User user = recipe.get().getUser();
    model.put("user", user);
    model.put("recipe", recipe.get());
    return "recipe/update";
}
}