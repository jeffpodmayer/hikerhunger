package com.coderscampus.hikerhunger.domain;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "recipeId")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    private String recipeName;
    private RecipeType recipeType;
    @Column(length=1000)
    private String instructions;
    private Integer servings;
    private Integer totalWeight;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("recipe")
    private List<Ingredient> ingredients = new ArrayList<>();
    @OneToMany(mappedBy = "recipe")
    private List<TripRecipe> tripRecipes = new ArrayList<>();
    private boolean isDeleted;


    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<TripRecipe> getTripRecipes() {
        return tripRecipes;
    }

    public void setTripRecipes(List<TripRecipe> tripRecipes) {
        this.tripRecipes = tripRecipes;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public enum RecipeType {
        BREAKFAST("Breakfast"),
        LUNCH("Lunch"),
        DINNER("Dinner"),
        SNACK("Snack"),
        EXTRA("Extra");
        private final String displayValue;
        RecipeType(String displayValue) {
            this.displayValue = displayValue;
        }
        public String getDisplayValue() {
            return displayValue;
        }
    }
    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", user=" + user +
                ", recipeName='" + recipeName + '\'' +
                ", recipeType=" + recipeType +
                ", instructions='" + instructions + '\'' +
                ", servings=" + servings +
                ", totalWeight=" + totalWeight +
                ", ingredients=" + ingredients +
                ", tripRecipes=" + tripRecipes +
                '}';
    }
}
