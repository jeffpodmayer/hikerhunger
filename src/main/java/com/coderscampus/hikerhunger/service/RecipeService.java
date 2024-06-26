package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.TripRecipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.repository.RecipeRepository;
import com.coderscampus.hikerhunger.repository.TripRecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepo;
    private final TripRecipeRepository tripRecipeRepo;

    @Autowired
    public RecipeService(RecipeRepository recipeRepo, TripRecipeRepository tripRecipeRepo) {
        this.recipeRepo = recipeRepo;
        this.tripRecipeRepo = tripRecipeRepo;
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

   @Transactional
    public void delete(Recipe recipe) {
        Optional<Recipe> optionalRecipe = recipeRepo.findById(recipe.getRecipeId());
        Recipe recipeToUpdateOrDelete = optionalRecipe.get();

        if(recipeToUpdateOrDelete.getTripRecipes().isEmpty()){
            recipeRepo.delete(recipe);
        } else {
            recipe.setDeleted(true);
            recipeRepo.save(recipe);
        }
    }

    public List<Recipe> findAll() {
       return recipeRepo.findAll();
    }

    public List<Recipe> findByUserId(Integer userId) {
        return recipeRepo.findByUserId(userId);
    }

    public void setRecipeServingsAndWeight(Recipe recipe){
            recipe.setServings(1);
            recipe.setTotalWeight(0);
    }

    public void updateRecipe(Recipe recipeData, Recipe recipe) {
            recipe.setRecipeName(recipeData.getRecipeName());
            recipe.setRecipeType(recipeData.getRecipeType());
            recipe.setInstructions(recipeData.getInstructions());
            recipe.setServings(recipeData.getServings());
            recipe.setTotalWeight(recipeData.getTotalWeight());
            saveRecipe(recipe);
    }

    public boolean isRecipeEmpty(Recipe recipe) {
        return recipe.getRecipeName() == null &&
                recipe.getRecipeType() == null &&
                recipe.getInstructions() == null &&
                recipe.getServings() == null &&
                recipe.getTotalWeight() == null;
    }
}
