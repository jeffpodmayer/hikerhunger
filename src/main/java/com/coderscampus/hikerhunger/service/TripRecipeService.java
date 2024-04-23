package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.TripRecipe;
import com.coderscampus.hikerhunger.repository.TripRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripRecipeService {

    private TripRecipeRepository tripRecipeRepo;
    @Autowired
    public TripRecipeService(TripRecipeRepository tripRecipesRepo) {
        this.tripRecipeRepo = tripRecipesRepo;
    }

    public void addTripRecipe(TripRecipe tripRecipe) {
        tripRecipeRepo.save(tripRecipe);
    }

    public void removeTripRecipe(TripRecipe tripRecipe) {
        tripRecipeRepo.delete(tripRecipe);
    }

    public void save(TripRecipe tripRecipe) {
        tripRecipeRepo.save(tripRecipe);
    }
}
