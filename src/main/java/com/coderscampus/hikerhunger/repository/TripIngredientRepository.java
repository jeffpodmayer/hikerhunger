package com.coderscampus.hikerhunger.repository;

import com.coderscampus.hikerhunger.domain.TripIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripIngredientRepository extends JpaRepository<TripIngredient, Long> {
}
