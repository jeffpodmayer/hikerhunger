package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.service.IngredientService;
import com.coderscampus.hikerhunger.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class IngredientController {

    private IngredientService ingredientService;

    private RecipeService recipeService;

    @Autowired
    public IngredientController(IngredientService ingredientService, RecipeService recipeService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
    }

    @PostMapping("/saveIngredients/{recipeId}")
    public ResponseEntity<List<Ingredient>> saveIngredients(@RequestBody List<Ingredient> ingredients, @PathVariable Long recipeId) {
        List<Ingredient> savedIngredients = ingredientService.saveIngredients(ingredients, recipeId);

        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipe.setIngredients(savedIngredients);
            recipeService.saveRecipe(recipe);
            System.out.println(recipe);
            return ResponseEntity.ok().body(savedIngredients);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
