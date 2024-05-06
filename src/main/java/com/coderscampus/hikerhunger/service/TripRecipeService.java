package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.TripRecipe;
import com.coderscampus.hikerhunger.repository.TripRecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripRecipeService {

    private final TripRecipeRepository tripRecipeRepo;

    public TripRecipeService(TripRecipeRepository tripRecipeRepo) {
        this.tripRecipeRepo = tripRecipeRepo;
    }

    public void save(TripRecipe tripRecipe) {
        tripRecipeRepo.save(tripRecipe);
    }

    public Optional<TripRecipe> findByTripAndRecipeId(Long tripId, Long recipeId) {
        return tripRecipeRepo.findByTripAndRecipeId(tripId, recipeId);
    }

    public void delete(TripRecipe tripRecipe) {
        tripRecipeRepo.delete(tripRecipe);
    }

    public List<TripRecipe> findByTripId(Long tripId) {
        return tripRecipeRepo.findByTripId(tripId);
    }
}
