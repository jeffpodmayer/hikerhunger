package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Recipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepo;

    private final UserServiceImpl userService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepo, UserServiceImpl userService) {
        this.recipeRepo = recipeRepo;
        this.userService = userService;
    }

    public Recipe save(Recipe recipe, Integer userId) {
        User user = userService.findUserById(userId).orElse(null);
        recipe.setUser(user);
        assert user != null;
        user.getRecipes().add(recipe);
        return recipeRepo.save(recipe);
    }
}
