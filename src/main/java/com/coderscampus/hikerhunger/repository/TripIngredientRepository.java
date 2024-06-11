package com.coderscampus.hikerhunger.repository;

import com.coderscampus.hikerhunger.domain.Ingredient;
import com.coderscampus.hikerhunger.domain.TripIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripIngredientRepository extends JpaRepository<TripIngredient, Long> {

    List<TripIngredient> findByIngredient(Ingredient ingredient);

    Optional<TripIngredient> findByIngredient_IngredientId(Long ingredientId);
}
