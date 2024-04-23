package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.TripRecipe;
import com.coderscampus.hikerhunger.repository.TripRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripRecipeService {

    private TripRecipeRepository tripRecipesRepo;
    @Autowired
    public TripRecipeService(TripRecipeRepository tripRecipesRepo) {
        this.tripRecipesRepo = tripRecipesRepo;
    }

    public void addTripRecipe(TripRecipe tripRecipe) {
        tripRecipesRepo.save(tripRecipe);
    }

    public void removeTripRecipe(TripRecipe tripRecipe) {
        tripRecipesRepo.delete(tripRecipe);
    }
}
