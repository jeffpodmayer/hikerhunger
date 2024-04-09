package com.coderscampus.hikerhunger.web;

import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/addRecipe")
    public String addNewRecipe(ModelMap model){
        model.put("recipe", new Recipe());
        return "recipe";
    }

    @PostMapping("/addRecipe")
    public String addNewRecipe(){
        return "home";
    }


}
