package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.TripRecipes;
import com.coderscampus.hikerhunger.repository.TripRecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripRecipesService {

    private TripRecipesRepository tripRecipesRepo;
    @Autowired
    public TripRecipesService( TripRecipesRepository tripRecipesRepo) {
        this.tripRecipesRepo = tripRecipesRepo;
    }

    public void addTripRecipe(TripRecipes tripRecipe) {
        tripRecipesRepo.save(tripRecipe);
    }

    public void removeTripRecipe(TripRecipes tripRecipe) {
        tripRecipesRepo.delete(tripRecipe);
    }
}
