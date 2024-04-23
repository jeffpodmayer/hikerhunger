package com.coderscampus.hikerhunger.repository;

import com.coderscampus.hikerhunger.domain.TripRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRecipeRepository extends JpaRepository<TripRecipe, Long> {
}
