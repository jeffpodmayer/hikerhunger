package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipeService {

    private RecipeRepository recipeRepo;

    private UserServiceImpl userService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepo, UserServiceImpl userService) {
        this.recipeRepo = recipeRepo;
        this.userService = userService;
    }

    public Recipe saveRecipe(Recipe recipe){
        recipeRepo.save(recipe);
        return recipe;
    }

    public Recipe createRecipe(Recipe recipe, User user) {
        recipe.setUser(user);
        user.getRecipes().add(recipe);
        return recipeRepo.save(recipe);
    }


    public Optional<Recipe> findById(Long recipeId) {
        return recipeRepo.findById(recipeId);
    }


}
