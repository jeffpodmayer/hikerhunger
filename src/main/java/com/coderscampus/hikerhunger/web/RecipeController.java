package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/addRecipe/{userId}")
    public String addNewRecipe(ModelMap model){
        model.put("recipe", new Recipe());
        return "recipe";
    }
}
