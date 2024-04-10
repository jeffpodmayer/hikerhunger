package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/createRecipe/{userId}")
    public String getCreateRecipe(ModelMap model){
        List<Ingredient> ingredients = new ArrayList<>();
        model.put("recipe", new Recipe());
        model.put("ingredients", ingredients);
        return "createRecipe";
    }

    @PostMapping("/createRecipe/{userId}")
    public String postCreateRecipe(Recipe recipe, @PathVariable Integer userId, @RequestParam List<Ingredient> ingredients){
        recipe.setIngredients(ingredients);
        recipeService.save(recipe, userId);
        System.out.println("Recipe that was posted:" + recipe);
        return "redirect:/home/" + userId ;
    }


}
