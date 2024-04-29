package com.coderscampus.hikerhunger.repository;

import com.coderscampus.hikerhunger.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long > {
    List<Recipe> findByUserId(Integer userId);
}
