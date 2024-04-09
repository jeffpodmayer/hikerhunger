package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/createRecipe/{userId}")
    public String addNewRecipe(ModelMap model){
        model.put("recipe", new Recipe());
        return "createRecipe";
    }

    @PostMapping("/createRecipe/{userId}")
    public String saveNewRecipe(Recipe recipe, @PathVariable Integer userId){
        System.out.println("Recipe that was posted:" + recipe);
        recipeService.save(recipe, userId);
        return "redirect:/home/" + userId;
    }


}
