package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.TripIngredient;
import com.coderscampus.hikerhunger.repository.TripIngredientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripIngredientService {
    private final TripIngredientRepository tripIngredientRepo;

    public TripIngredientService(TripIngredientRepository tripIngredientRepo) {
        this.tripIngredientRepo = tripIngredientRepo;
    }

    public void save(TripIngredient tripIngredient) {
        tripIngredientRepo.save(tripIngredient);
    }

    public Optional<TripIngredient> findById(Long ingredientId) {
        return tripIngredientRepo.findById(ingredientId);
    }
}
