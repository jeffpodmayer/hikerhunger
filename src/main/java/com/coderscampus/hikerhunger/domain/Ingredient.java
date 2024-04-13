package com.coderscampus.hikerhunger.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;
    @ManyToOne
    @JoinColumn(name="recipe_id")
    private Recipe recipe;
    private String ingredientName;
    private Float quantity;
    private String unit;
    private Float weightInGrams;
    private String notes;

//    public Ingredient(Long ingredientId, Recipe recipe, String ingredientName, Float quantity, String unit, Float weightInGrams, String notes) {
//        this.ingredientId = ingredientId;
//        this.recipe = recipe;
//        this.ingredientName = ingredientName;
//        this.quantity = quantity;
//        this.unit = unit;
//        this.weightInGrams = weightInGrams;
//        this.notes = notes;
//    }

//    @Override
//    public String toString() {
//        return "Ingredient{" +
//                "ingredientId=" + ingredientId +
//                ", recipe=" + recipe +
//                ", ingredientName='" + ingredientName + '\'' +
//                ", quantity=" + quantity +
//                ", unit='" + unit + '\'' +
//                ", weightInGrams=" + weightInGrams +
//                ", notes='" + notes + '\'' +
//                '}';
//    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(Float weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
