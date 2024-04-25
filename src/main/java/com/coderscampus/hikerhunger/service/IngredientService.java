package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepo;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        ingredientRepo.save(ingredient);
        return ingredient;
    }

    public void delete(Ingredient ingredient) {
        ingredientRepo.delete(ingredient);
    }

    public Optional<Ingredient> findById(Long ingredientId) {
        return ingredientRepo.findById(ingredientId);
    }

}
