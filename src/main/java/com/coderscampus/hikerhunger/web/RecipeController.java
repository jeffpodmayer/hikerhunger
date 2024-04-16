package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.service.IngredientService;
import com.coderscampus.hikerhunger.service.RecipeService;
import com.coderscampus.hikerhunger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        System.out.println("Created new recipe:" + newRecipe);
        return "redirect:/home/" + userId + "/recipe/" + newRecipe.getRecipeId();
    }

    @GetMapping("/{userId}/recipe/{recipeId}")
    public String getCreateRecipe(ModelMap model, @PathVariable Integer userId, @PathVariable Long recipeId) {
        User user = userService.findById(userId);
        Optional<Recipe> recipe = recipeService.findById(recipeId);
        model.put("user", user);
        model.put("recipe", recipe);
        return "recipe/create";
    }

    @PostMapping("/saveRecipe/{recipeId}")
    public ResponseEntity<Recipe> saveRecipe(@RequestBody Recipe recipeData, @PathVariable Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            try {
                recipe.setRecipeName(recipeData.getRecipeName());
                recipe.setRecipeType(recipeData.getRecipeType());
                recipe.setInstructions(recipeData.getInstructions());
                recipe.setServings(recipeData.getServings());
                recipe.setTotalWeight(recipeData.getTotalWeight());

                // Set the recipe for each ingredient
                List<Ingredient> ingredients = recipeData.getIngredients();
                for (Ingredient ingredient : ingredients) {
                    ingredient.setRecipe(recipe);
                }
                recipe.setIngredients(ingredients); // Set the updated ingredient list


                Recipe savedRecipe = recipeService.saveRecipe(recipe);
                System.out.println("Sent from createRecipe page:" + savedRecipe);

                return ResponseEntity.ok().body(savedRecipe);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/updateRecipe/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipeData, @PathVariable Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            try {
                // Update recipe information
                recipe.setRecipeName(recipeData.getRecipeName());
                recipe.setRecipeType(recipeData.getRecipeType());
                recipe.setInstructions(recipeData.getInstructions());
                recipe.setServings(recipeData.getServings());
                recipe.setTotalWeight(recipeData.getTotalWeight());

                // Get existing ingredients associated with the recipe
                List<Ingredient> existingIngredients = recipe.getIngredients();

                System.out.println(existingIngredients);

                // Update existing ingredient or add new one
                for (Ingredient ingredient : recipeData.getIngredients()) {
                    if (ingredient.getIngredientId() != null) {
                        // If the ingredient has an ID, it's an existing ingredient
                        // Find the corresponding existing ingredient and update its properties
                        Optional<Ingredient> optionalExistingIngredient = existingIngredients.stream()
                                .filter(i -> i.getIngredientId().equals(ingredient.getIngredientId()))
                                .findFirst();
                        if (optionalExistingIngredient.isPresent()) {
                            Ingredient existingIngredient = optionalExistingIngredient.get();
                            existingIngredient.setIngredientName((ingredient.getIngredientName()));
                            existingIngredient.setQuantity(ingredient.getQuantity());
                            existingIngredient.setUnit(ingredient.getUnit());
                            existingIngredient.setWeightInGrams(ingredient.getWeightInGrams());
                            existingIngredient.setNotes(ingredient.getNotes());
//                        } else {
//                            // If the ingredient doesn't exist in the existingIngredients list, add it
//                            existingIngredients.add(ingredient);
                        }
                    } else {
                        // If the ingredient doesn't have an ID, it's a new ingredient
                        // Set the recipe for the new ingredient and add it to the list of existing ingredients
                        ingredient.setRecipe(recipe);
                        existingIngredients.add(ingredient);
                    }
                }

                // Save the updated recipe
                Recipe savedRecipe = recipeService.saveRecipe(recipe);
                System.out.println("Updated Recipe: " + savedRecipe);

                return ResponseEntity.ok().body(recipe);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
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

//    @GetMapping("/edit-recipe/{recipeId}/ingredients")
//    @ResponseBody
//    public List<Ingredient> getIngredientsForRecipe(@PathVariable Long recipeId) {
//        Optional<Recipe> recipe = recipeService.findById(recipeId);
//        if (recipe.isPresent()) {
//            return recipe.get().getIngredients();
//        } else {
//            return Collections.emptyList();
//        }
//    }


}
