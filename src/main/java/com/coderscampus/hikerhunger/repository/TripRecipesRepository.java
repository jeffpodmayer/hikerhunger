package com.coderscampus.hikerhunger.repository;

import com.coderscampus.hikerhunger.domain.Trip;
import com.coderscampus.hikerhunger.domain.TripRecipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRecipesRepository extends JpaRepository<TripRecipes, Long> {
}
