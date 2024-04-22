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
        return "redirect:/home/" + userId + "/recipe/" + newRecipe.getRecipeId();
    }

    @GetMapping("/{userId}/recipe/{recipeId}") // REFACTOR
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

    @PostMapping("/saveRecipe/{recipeId}") // REFACTOR!
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

    @PostMapping("/deleteRecipe/{recipeId}") // REFACTOR BODY OF THIS METHOD AND NEXT POST MAPPING BELOW
    public ResponseEntity<Void> deleteRecipeIcon(@PathVariable Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipeService.delete(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/postDeleteRecipe/{recipeId}")
    public String postDeleteRecipe(@PathVariable Long recipeId){
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
        try {
            Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

            if (optionalRecipe.isPresent()) {
                Recipe recipe = optionalRecipe.get();
                recipe.setRecipeName(recipeData.getRecipeName());
                recipe.setRecipeType(recipeData.getRecipeType());
                recipe.setInstructions(recipeData.getInstructions());
                recipe.setServings(recipeData.getServings());
                recipe.setTotalWeight(recipeData.getTotalWeight());
                recipeService.saveRecipe(recipe);

                return "redirect:/home/" + recipe.getUser().getId();
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
    List<Ingredient> ingredients = recipe.get().getIngredients();
    model.put("user", user);
    model.put("recipe", recipe.get());
    model.put("ingredients", ingredients);
    return "recipe/update";
}
}