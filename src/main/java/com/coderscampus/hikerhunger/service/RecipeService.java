package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepo;

    @Autowired
    public RecipeService(RecipeRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
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

    public void delete(Recipe recipe) {
        recipeRepo.delete(recipe);
    }

    public List<Recipe> findAll() {
       return recipeRepo.findAll();
    }

    public List<Recipe> findByUserId(Integer userId) {
        return recipeRepo.findByUserId(userId);
    }
}
