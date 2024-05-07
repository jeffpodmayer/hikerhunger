package com.coderscampus.hikerhunger.repository;

import com.coderscampus.hikerhunger.domain.TripRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRecipeRepository extends JpaRepository<TripRecipe,Long> {
    @Query("SELECT tr FROM TripRecipe tr WHERE tr.trip.id = :tripId AND tr.recipe.id = :recipeId")
    Optional<TripRecipe> findByTripAndRecipeId(Long tripId, Long recipeId);

    @Query("SELECT tr FROM TripRecipe tr WHERE tr.trip.tripId = :tripId")
    List<TripRecipe> findByTripId(Long tripId);

    @Query("SELECT tr FROM TripRecipe tr WHERE tr.recipe.id= :recipeId")
    List<TripRecipe> findByRecipeId(Long recipeId);
}
