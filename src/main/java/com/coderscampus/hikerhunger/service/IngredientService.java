package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientService {

    private IngredientRepository ingredientRepo;
    private RecipeService recipeService;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepo, RecipeService recipeService) {
        this.ingredientRepo = ingredientRepo;
        this.recipeService = recipeService;
    }

    public void saveIngredient(Ingredient ingredient) {
        ingredientRepo.save(ingredient);
    }
//    @Transactional
//    public void clearRecipeIngredients(Recipe recipe) {
//        List<Ingredient> ingredients = recipe.getIngredients();
//        if (!ingredients.isEmpty()) {
//            ingredientRepo.deleteAll(ingredients);
//        }
//    }

    public void delete(Ingredient ingredient) {
        ingredientRepo.delete(ingredient);
    }

    public void deleteById(Long ingredientId) {
        ingredientRepo.deleteById(ingredientId);
    }


    public void deleteIngredientById(Long ingredientId) {
        ingredientRepo.deleteById(ingredientId);
    }

    public Optional<Ingredient> findById(Long ingredientId) {
        return ingredientRepo.findById(ingredientId);
    }
//    public void deleteIngredient(Ingredient ingredient) {
//        System.out.println("Deleted ingredient");
//        ingredientRepo.delete(ingredient);
//    }


//    public List<Ingredient> saveIngredients(List<Ingredient> newIngredients, Long recipeId) {
//        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
//        // Check if the recipe exists
//        if (optionalRecipe.isPresent()) {
//            Recipe recipe = optionalRecipe.get();
//
//            List<Ingredient> existingIngredients = recipe.getIngredients();
//            existingIngredients.clear();
//
//            // Associate each ingredient with the recipe and save it
//            for (Ingredient ingredient : newIngredients) {
//                ingredient.setRecipe(recipe);
//                newIngredients.add(ingredientRepo.save(ingredient));
//            }
//            // Return the list of saved ingredients
//            return newIngredients;
//        } else {
//            // Recipe with the given ID does not exist
//            throw new RuntimeException("Recipe with ID " + recipeId + " not found");
//        }
//    }
}
