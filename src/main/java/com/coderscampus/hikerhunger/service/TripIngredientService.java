package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.TripIngredient;
import com.coderscampus.hikerhunger.repository.TripIngredientRepository;
import org.springframework.stereotype.Service;

@Service
public class TripIngredientService {
    private final TripIngredientRepository tripIngredientRepo;

    public TripIngredientService(TripIngredientRepository tripIngredientRepo) {
        this.tripIngredientRepo = tripIngredientRepo;
    }

    public void save(TripIngredient tripIngredient) {
        tripIngredientRepo.save(tripIngredient);
    }
}
